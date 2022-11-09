package com.fastfood.entity.order;


import com.fastfood.entity.food.Food;
import com.fastfood.template.EntityClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "order_item")
public class OrderItem extends EntityClass {

    @ManyToOne
    private Food food;

    private Integer quantity;

    private Double unitPrice;

}
