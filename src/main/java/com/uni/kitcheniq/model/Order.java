package com.uni.kitcheniq.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "order_details", nullable = false)
    private String details;

    @Column(name = "order_price", nullable = false)
    private double price;

    @Column(name = "order_bill", nullable = false)
    private String bill;

    @Column(name = "order_status", nullable = false)
    private String status = "PENDING";

    @CreationTimestamp
    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDate orderDate;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "component_id")
    )
    private List<OrderComponent> components = new ArrayList<>();

    public Order(Long id, String details, double price, String bill, String status, List<OrderComponent> components) {
        this.id = id;
        this.details = details;
        this.price = price;
        this.bill = bill;
        this.status = status;
        this.components = components != null ? components : new ArrayList<>();
    }

    public Order(String details, double price, String bill, String status) {
        this.details = details;
        this.price = price;
        this.bill = bill;
        this.status = status;
    }

    public Order() {
        // Default constructor
    }

    public Long getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public List<OrderComponent> getComponents() {
        return components;
    }

    public void setComponents(List<OrderComponent> components) {
        this.components = components;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setId(Long id) {
        this.id = id;
    }
}