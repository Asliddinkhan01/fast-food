package com.fastfood.entity.order;


import com.fastfood.entity.user.User;
import com.fastfood.template.EntityClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "orders")
public class Order extends EntityClass {

    @ManyToOne
    private User user;

    @OneToMany
    @JoinTable(name = "orders_order_items",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "order_item_id")})
    private List<OrderItem> foods;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @Column(columnDefinition = "boolean default false")
    private boolean isReady;

    @Column(columnDefinition = "boolean default false")
    private boolean isDelivered;

}
