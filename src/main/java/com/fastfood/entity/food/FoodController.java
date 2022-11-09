package com.fastfood.entity.food;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("${app.domain}/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/downloadMenu")
    public void downloadMenu(HttpServletResponse res) {
        foodService.downloadMenu(res);
    }

    @GetMapping("get/{foodId}")
    public HttpEntity<?> getFoodById(@PathVariable UUID foodId) {
        return foodService.getFoodById(foodId);
    }

    @GetMapping("/get")
    public HttpEntity<?> getAllFoods(
            @RequestParam(defaultValue = "") String search) {
        return foodService.getAllFood(search);
    }

    @GetMapping("/category")
    public HttpEntity<?> getFoodsByCategory(@RequestParam String category) {
        return foodService.getFoodsByCategory(category);
    }

    @PreAuthorize("hasAnyRole('Admin','Chef')")
    @PutMapping("/{foodId}")
    public HttpEntity<?> editFood(
            @RequestPart("food") FoodDto foodDto,
            @RequestPart("file") MultipartFile file,
            @PathVariable UUID foodId) {
        return foodService.editFood(foodDto, file, foodId);
    }

    @PreAuthorize("hasAnyRole('Admin','Chef')")
    @PutMapping("/content/{foodId}")
    public HttpEntity<?> editFoodContent(
            @RequestPart("food") FoodDto2 foodDto,
            @PathVariable UUID foodId
    ){
        return foodService.editFoodContent(foodId, foodDto);
    }

    @PreAuthorize("hasAnyRole('Admin','Chef')")
    @PostMapping
    public HttpEntity<?> addFood(
            @RequestPart("food") FoodDto foodDto,
            @RequestPart("file") MultipartFile file) {
        return foodService.addFood(foodDto, file);
    }

}
