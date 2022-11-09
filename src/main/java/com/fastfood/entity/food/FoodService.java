package com.fastfood.entity.food;


import com.fastfood.common.ApiResponse;
import com.fastfood.entity.attachment.Attachment;
import com.fastfood.entity.attachment.AttachmentService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    private final AttachmentService attachmentService;

    private boolean saveCategoryAttachment(List<String> categoriesDto, MultipartFile file, Food food) {
        try {
            List<Category> categories = getCategories(categoriesDto);
            food.setCategories(categories);

            Attachment attachment = attachmentService.saveAttachment(file);
            food.setPhoto(attachment);

            foodRepository.save(food);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    private List<Category> getCategories(List<String> names) {
        List<Category> res = new ArrayList<>();

        for (String name : names) {
            name = name.toUpperCase();
            switch (name) {
                case "BEST_SELLER" -> res.add(Category.BEST_SELLER);
                case "LAVASH" -> res.add(Category.LAVASH);
                case "SHAURMA" -> res.add(Category.SHAURMA);
                case "BURGER" -> res.add(Category.BURGER);
                case "HOTDOG" -> res.add(Category.HOTDOG);
                case "MIXED" -> res.add(Category.MIXED);
                case "COMBO" -> res.add(Category.COMBO);
                case "DESSERT" -> res.add(Category.DESSERT);
                case "SAUCE" -> res.add(Category.SAUCE);
                case "DRINK" -> res.add(Category.DRINK);
            }
        }

        return res;
    }

    public HttpEntity<?> addFood(FoodDto foodDto, MultipartFile file) {
        Food food = new Food();

        food.setName(foodDto.getName());

        if (foodDto.getDescription() != null)
            food.setDescription(foodDto.getDescription());

        food.setPrice(foodDto.getPrice());

        if (saveCategoryAttachment(foodDto.getCategories(), file, food))
            return new ResponseEntity<>(new ApiResponse("Something went wrong", false), HttpStatus.CONFLICT);

        createMenu();
        return new ResponseEntity<>(new ApiResponse("Created", true), HttpStatus.CREATED);
    }

    public HttpEntity<?> editFood(FoodDto foodDto, MultipartFile file, UUID foodId) {
        Optional<Food> optionalFood = foodRepository.findById(foodId);

        if (optionalFood.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Food not found", false), HttpStatus.NOT_FOUND);
        }

        Food food = optionalFood.get();

        food.setName(foodDto.getName());
        food.setPrice(foodDto.getPrice());

        if (foodDto.getDescription() != null)
            food.setDescription(foodDto.getDescription());

        attachmentService.deletePhoto(food.getPhoto().getId());

        if (saveCategoryAttachment(foodDto.getCategories(), file, food))
            return new ResponseEntity<>(new ApiResponse("Something went wrong", false), HttpStatus.CONFLICT);

        createMenu();
        return new ResponseEntity<>(new ApiResponse("Edited success", true), HttpStatus.ACCEPTED);
    }

    public HttpEntity<?> getFoodById(UUID foodId) {
        Optional<FoodProjection> optionalFood = foodRepository.getFoodById(foodId);

        if (optionalFood.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Food not found", false), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ApiResponse("Success", true, optionalFood.get()), HttpStatus.OK);
    }

    public HttpEntity<?> getAllFood(String search) {
        List<FoodProjection> allFoods = foodRepository.getAllFoods(search);

        return new ResponseEntity<>(new ApiResponse("Success", true, allFoods), HttpStatus.OK);
    }

    public HttpEntity<?> getFoodsByCategory(String category) {
        List<FoodProjection> foodsByCategory = foodRepository.getFoodsByCategory(category);
        return new ResponseEntity<>(new ApiResponse("Success", true, foodsByCategory), HttpStatus.OK);
    }

    public void downloadMenu(HttpServletResponse res) {
        try {
            res.setHeader("Content-Disposition", "attachment; filename=menu.pdf");
            res.setHeader("Content-Transfer-Encoding", "binary");
            BufferedOutputStream outputStream = new BufferedOutputStream(res.getOutputStream());
            FileInputStream inputStream = new FileInputStream("src/main/resources/menu/menu.pdf");
            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.close();
            res.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createMenu() {
        List<Food> allFood = foodRepository.findAll();

        try (PdfWriter pdfWriter = new PdfWriter("src/main/resources/menu/menu.pdf")) {
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            pdfDocument.setDefaultPageSize(PageSize.A4);

            pdfDocument.addNewPage();
            Document document = new Document(pdfDocument);
            Paragraph paragraph = new Paragraph("Menu");
            paragraph.setTextAlignment(TextAlignment.CENTER);
            document.add(paragraph);

            float[] colWidth = {80F, 150F, 150F, 150F, 150F};
            Table table = new Table(colWidth);

            table.addCell(new Cell().add("T/R"));
            table.addCell(new Cell().add("Image"));
            table.addCell(new Cell().add("Name"));
            table.addCell(new Cell().add("Description"));
            table.addCell(new Cell().add("Price"));

            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.setTextAlignment(TextAlignment.CENTER);

            String url = "src/main/resources/photo/";
            int count = 1;

            for (Food food : allFood) {
                ImageData imageDataFactory = ImageDataFactory.create(url + food.getPhoto().getName());
                Image image = new Image(imageDataFactory);
                image.scaleToFit(120f, 60f);
                image.setRelativePosition(1f, 1f, 1f, 1f);

                table.addCell(new Cell().add(String.valueOf(count++)));
                table.addCell(new Cell().add(image));
                table.addCell(new Cell().add(food.getName()));
                table.addCell(new Cell().add(food.getDescription()));
                table.addCell(new Cell().add(String.valueOf(food.getPrice())));
            }

            document.add(table);
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpEntity<?> editFoodContent(UUID foodId, FoodDto2 foodDto) {
        Optional<Food> optionalFood = foodRepository.findById(foodId);

        if (optionalFood.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Food not found", false), HttpStatus.NOT_FOUND);
        }

        Food food = optionalFood.get();

        List<Category> categories = getCategories(foodDto.getCategories());

        food.setPrice(foodDto.getPrice());
        food.setCategories(categories);

        foodRepository.save(food);
        return new ResponseEntity<>(new ApiResponse("Saved success", true), HttpStatus.ACCEPTED);
    }
}
