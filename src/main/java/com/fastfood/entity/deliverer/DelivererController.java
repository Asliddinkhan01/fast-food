package com.fastfood.entity.deliverer;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${app.domain}/deliverer")
@RequiredArgsConstructor
public class DelivererController {

    private final DelivererService delivererService;

    @PreAuthorize("hasRole('Deliverer')")
    @GetMapping("/{orderId}")
    public HttpEntity<?> getOrderById(@PathVariable UUID orderId) {
        return delivererService.getOrderById(orderId);
    }

    @PreAuthorize("hasRole('Deliverer')")
    @GetMapping
    public HttpEntity<?> getOrders() {
        return delivererService.getOrders();
    }

    @PreAuthorize("hasRole('Deliverer')")
    @PatchMapping("/{orderId}")
    private HttpEntity<?> makeOrderDelivered(@PathVariable UUID orderId) {
        return delivererService.makeOrderDelivered(orderId);
    }
}
