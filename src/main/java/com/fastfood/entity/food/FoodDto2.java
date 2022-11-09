package com.fastfood.entity.food;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodDto2 {

    private double price;

    private List<String> categories;

}
