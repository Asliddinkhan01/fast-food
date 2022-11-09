package com.fastfood.entity.order;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    @Query(nativeQuery = true,
            value = "select *\n" +
                    "from order_item\n" +
                    "where food_id = :foodId\n" +
                    "  and quantity = :quantity")
    Optional<OrderItem> getByFoodIdAndQuantity(UUID foodId, int quantity);
}
