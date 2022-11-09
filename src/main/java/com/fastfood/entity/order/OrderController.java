package com.fastfood.entity.order;


import com.fastfood.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.domain}/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public HttpEntity<?> orderFood(@RequestPart("orderDto") OrderDto orderDto) {
        return orderService.orderFood(orderDto);
    }

    @GetMapping("/history")
    public HttpEntity<?> getOrderHistory(@AuthenticationPrincipal User currentUser) {
        return orderService.getOrderHistory(currentUser.getId());
    }

}
