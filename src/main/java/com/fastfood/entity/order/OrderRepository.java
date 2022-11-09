package com.fastfood.entity.order;

import com.fastfood.entity.order.projections.OrderItemProjection;
import com.fastfood.entity.order.projections.OrderProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {


    @Query(nativeQuery = true,
            value = "select f.name        as foodName,\n" +
                    "       oi.quantity   as quantity,\n" +
                    "       oi.unit_price as unitPrice\n" +
                    "from orders o\n" +
                    "         join orders_order_items ooi on o.id = ooi.order_id\n" +
                    "         join order_item oi on ooi.order_item_id = oi.id\n" +
                    "         join foods f on oi.food_id = f.id\n" +
                    "where o.id = :orderId\n" +
                    "  and o.is_ready = false\n" +
                    "  and o.is_delivered = false")
    List<OrderItemProjection> getOrderItemsById(UUID orderId);

    @Query(nativeQuery = true,
            value = "select cast(o.id as varchar) as id,\n" +
                    "       o.order_date          as orderDate,\n" +
                    "       o.address             as address,\n" +
                    "       u.phone_number        as userPhone\n" +
                    "       o.is_delivered        as isDelivered\n" +
                    "       o.is_ready        as isReady\n" +
                    "from orders o\n" +
                    "         join users u on o.user_id = u.id\n" +
                    "where is_ready = false\n" +
                    "  and is_delivered = false\n" +
                    "order by order_date")
    List<OrderProjection> getAllOrders();


    @Query(nativeQuery = true,
            value = "select cast(o.id as varchar) as id,\n" +
                    "       o.order_date          as orderDate,\n" +
                    "       o.address             as address,\n" +
                    "       u.phone_number        as userPhone\n" +
                    "       o.is_delivered        as isDelivered\n" +
                    "       o.is_ready        as isReady\n" +
                    "from orders o\n" +
                    "         join users u on o.user_id = u.id\n" +
                    "where o.is_ready = false\n" +
                    "  and o.is_delivered = false\n" +
                    "  and o.id = :orderId")
    Optional<OrderProjection> getOrderById(UUID orderId);

    @Query(nativeQuery = true,
            value = "select cast(o.id as varchar) as id,\n" +
                    "       o.order_date          as orderDate,\n" +
                    "       o.address             as address,\n" +
                    "       u.phone_number        as userPhone\n" +
                    "       o.is_delivered        as isDelivered\n" +
                    "       o.is_ready        as isReady\n" +
                    "from orders o\n" +
                    "         join users u on o.user_id = u.id\n" +
                    "where o.is_ready = false\n" +
                    "  and o.is_delivered = false\n" +
                    "  and o.user_id = :userId")
    List<OrderProjection> getOrderByUserId(UUID userId);
}
