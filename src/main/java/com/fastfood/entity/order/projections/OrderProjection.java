package com.fastfood.entity.order.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;

public interface OrderProjection {

    UUID getId();

    String getOrderDate();

    String getAddress();

    String getUserPhone();

    boolean getIsDelivered();

    boolean getIsReady();


    @Value("#{@orderRepository.getOrderItemsById(target.id)}")
    List<OrderItemProjection> getFoods();

}
