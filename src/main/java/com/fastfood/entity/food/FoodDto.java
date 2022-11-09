package com.fastfood.entity.food;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodDto {

    @NotNull(message = "Required field")
    @NotBlank(message = "Name cannot be blank.")
    private String name;

    private String description;

    @NotNull(message = "Required field")
    @NotBlank(message = "Price cannot be blank.")
    private double price;


    @NotNull(message = "Required field")
    @NotBlank(message = "Categories cannot be blank.")
    private List<String> categories;

}
