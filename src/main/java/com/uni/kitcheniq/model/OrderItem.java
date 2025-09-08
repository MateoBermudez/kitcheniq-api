package com.uni.kitcheniq.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private MenuComponent component;

    @Column(nullable = false)
    private int quantity;

    public OrderItem() {}

    public OrderItem(Order order, MenuComponent component, int quantity) {
        this.order = order;
        this.component = component;
        this.quantity = quantity;
    }

    // getters and setters
}
