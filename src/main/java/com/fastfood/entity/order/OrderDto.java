package com.fastfood.entity.order;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {

    List<OrderItemDto> foods;

    @NotNull(message = "Required field")
    @NotBlank(message = "UserId cannot be blank.")
    private UUID userId;

    @NotNull(message = "Required field")
    @NotBlank(message = "OrderDate cannot be blank.")
    private LocalDateTime orderDate;

    @NotNull(message = "Required field")
    @NotBlank(message = "Address cannot be blank.")
    private String address;

}
