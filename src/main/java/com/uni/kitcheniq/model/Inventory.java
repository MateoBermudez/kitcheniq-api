package com.uni.kitcheniq.model;

import com.uni.kitcheniq.exception.InsufficientInventory;
import jakarta.persistence.*;

@Entity(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, unique = true)
    private String name;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public Inventory() {
        // Default constructor
    }

    public Inventory(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Inventory(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String productName) {
        this.name = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void deductQuantity(int amount) {
        if (quantity >= amount) {
            quantity -= amount;
        } else {
            throw new InsufficientInventory("Insufficient " + name + " in inventory. Available: " + quantity + ", Requested: " + amount);
        }
    }

    public void addQuantity(Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Cannot add negative quantity to inventory.");
        }
        this.quantity += quantity;
    }
}