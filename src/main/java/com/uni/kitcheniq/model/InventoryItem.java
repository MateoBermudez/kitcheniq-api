package com.uni.kitcheniq.model;

import jakarta.persistence.*;

@Entity
@Table(name="InventoryItem")
public class InventoryItem {

    @Id
    @Column(name="Id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name="Name", nullable = false)
    private String Name;

    @Column(name="Quantity", nullable = false)
    private int Quantity;

    public InventoryItem() {
        // Default constructor
    }

    public InventoryItem(Long id, String name, int quantity) {
        Id = id;
        Name = name;
        Quantity = quantity;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
