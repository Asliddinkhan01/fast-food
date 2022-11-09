package com.fastfood.entity.chef;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${app.domain}/chef")
@RequiredArgsConstructor
public class ChefController {

    private final ChefService chefService;

    @PreAuthorize("hasRole('Chef')")
    @GetMapping("/{orderId}")
    public HttpEntity<?> getOrderById(@PathVariable UUID orderId) {
        return chefService.getOrderById(orderId);
    }

    @PreAuthorize("hasRole('Chef')")
    @GetMapping
    public HttpEntity<?> getOrders() {
        return chefService.getOrders();
    }

    @PreAuthorize("hasRole('Chef')")
    @PatchMapping("/{orderId}")
    private HttpEntity<?> makeOrderDone(@PathVariable UUID orderId) {
        return chefService.makeOrderDone(orderId);
    }
}
