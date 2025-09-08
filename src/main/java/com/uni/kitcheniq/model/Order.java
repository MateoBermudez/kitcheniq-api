package com.uni.kitcheniq.model;

import com.uni.kitcheniq.enums.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Orders")
public class Order {
    @Id
    @Column(name="Id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "order_details", nullable = false)
    private String details;

    @Column(name = "order_price", nullable = false)
    private double price;

    @Column(name = "order_bill", nullable = false)
    private String bill;

    OrderStatus status;

    @CreationTimestamp
    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDate orderDate;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;


    public Order() {
        // Default constructor
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
