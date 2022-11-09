package com.fastfood.entity.food;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;

public interface FoodProjection {

    UUID getId();

    String getName();

    String getDescription();

    int getPrice();

    @Value("#{@attachmentRepository.getNameById(target.id )}")
    String getPhotoName();

    @Value("#{@foodRepository.getCategoriesById(target.id)}")
    List<String> getCategories();

}
