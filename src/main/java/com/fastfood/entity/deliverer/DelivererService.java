package com.fastfood.entity.deliverer;


import com.fastfood.common.ApiResponse;
import com.fastfood.entity.order.Order;
import com.fastfood.entity.order.OrderRepository;
import com.fastfood.entity.order.projections.OrderProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DelivererService {

    private final OrderRepository orderRepository;

    public HttpEntity<?> getOrders() {
        List<OrderProjection> allOrders = orderRepository.getAllOrders();
        return new ResponseEntity<>(new ApiResponse("Success", true, allOrders), HttpStatus.OK);
    }

    public HttpEntity<?> makeOrderDelivered(UUID orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isEmpty())
            return new ResponseEntity<>(new ApiResponse("Order not found", false), HttpStatus.NOT_FOUND);

        Order order = optionalOrder.get();
        order.setDelivered(true);
        orderRepository.save(order);

        return new ResponseEntity<>(new ApiResponse("Done", true), HttpStatus.ACCEPTED);
    }

    public HttpEntity<?> getOrderById(UUID orderId) {
        Optional<OrderProjection> optionalOrder = orderRepository.getOrderById(orderId);

        if (optionalOrder.isEmpty())
            return new ResponseEntity<>(new ApiResponse("Not found", false), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new ApiResponse("Success", true, optionalOrder.get()), HttpStatus.OK);
    }
}
