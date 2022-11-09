package com.fastfood.entity.order;


import com.fastfood.common.ApiResponse;
import com.fastfood.entity.food.Food;
import com.fastfood.entity.food.FoodRepository;
import com.fastfood.entity.order.projections.OrderProjection;
import com.fastfood.entity.user.User;
import com.fastfood.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private List<OrderItem> getOrderItems(List<OrderItemDto> foods) {
        List<OrderItem> res = new ArrayList<>();
        for (OrderItemDto orderItemDto : foods) {
            Optional<Food> optionalFood = foodRepository.findById(orderItemDto.getFoodId());
            if (optionalFood.isEmpty()) return null;
            Food food = optionalFood.get();

            Optional<OrderItem> optionalOrderItem = orderItemRepository.getByFoodIdAndQuantity(orderItemDto.getFoodId(), orderItemDto.getQuantity());

            if (optionalOrderItem.isPresent()) {
                OrderItem orderItem = optionalOrderItem.get();
                orderItem.setUnitPrice(food.getPrice() * orderItemDto.getQuantity());
                OrderItem saved = orderItemRepository.save(orderItem);
                res.add(saved);
            } else {

                OrderItem orderItem = new OrderItem();

                orderItem.setFood(food);
                orderItem.setQuantity(orderItemDto.getQuantity());
                orderItem.setUnitPrice(orderItemDto.getQuantity() * food.getPrice());

                OrderItem saved = orderItemRepository.save(orderItem);
                res.add(saved);
            }
        }

        return res;
    }

    public HttpEntity<?> orderFood(OrderDto orderDto) {
        Optional<User> optionalUser = userRepository.findById(orderDto.getUserId());
        if (optionalUser.isEmpty())
            return new ResponseEntity<>(new ApiResponse("User Not found", false), HttpStatus.NOT_FOUND);

        User user = optionalUser.get();

        List<OrderItem> orderItems = getOrderItems(orderDto.getFoods());

        if (orderItems == null)
            return new ResponseEntity<>(new ApiResponse("Something went wrong", false), HttpStatus.CONFLICT);

        Order order = new Order();

        order.setUser(user);
        order.setFoods(orderItems);
        order.setOrderDate(orderDto.getOrderDate());
        order.setAddress(orderDto.getAddress());

        orderRepository.save(order);

        return new ResponseEntity<>(new ApiResponse("Ordered success", true), HttpStatus.CREATED);
    }

    public HttpEntity<?> getOrderHistory(UUID userId) {
        List<OrderProjection> ordersByUserId = orderRepository.getOrderByUserId(userId);
        return new ResponseEntity<>(new ApiResponse("Success", true, ordersByUserId), HttpStatus.OK);
    }
}
