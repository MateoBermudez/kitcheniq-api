package com.uni.kitcheniq.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_bill", nullable = false)
    private String orderBill;

    @Column(name = "order_details", nullable = false)
    private String orderDetails;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "order_price", nullable = false)
    private Double orderPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderBill() {
        return orderBill;
    }

    public void setOrderBill(String orderBill) {
        this.orderBill = orderBill;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

}