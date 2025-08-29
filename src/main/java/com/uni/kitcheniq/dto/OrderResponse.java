package com.uni.kitcheniq.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderResponse {

    private Long id;
    private String details;
    private double price;
    private String bill;
    private String status;
    private LocalDate orderDate;
    private List<String> items;

    public OrderResponse(Long id, String details, double price, String bill, String status, LocalDate orderDate, List<String> items) {
        this.id = id;
        this.details = details;
        this.price = price;
        this.bill = bill;
        this.status = status;
        this.orderDate = orderDate;
        this.items = items;
    }

    public OrderResponse() {
        // Default constructor
    }

    public OrderResponse(String details, double price, String bill, String status, LocalDate orderDate, List<String> items) {
        this.details = details;
        this.price = price;
        this.bill = bill;
        this.status = status;
        this.orderDate = orderDate;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
