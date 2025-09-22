package com.uni.kitcheniq.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private MenuComponent component;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}