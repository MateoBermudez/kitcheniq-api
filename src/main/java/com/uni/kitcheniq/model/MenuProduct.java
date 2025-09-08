package com.uni.kitcheniq.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "MenuProduct")
public class MenuProduct extends MenuComponent {

    @Column (name = "Name", nullable = false)
    private String Name;

    @Column (name = "Price", nullable = false)
    private double Price;

    @OneToMany(mappedBy = "menuProduct", cascade = CascadeType.ALL)
    private Set<MenuProductInventoryItem> Items;

    public MenuProduct() {
        // Default constructor
    }

    @Override
    public double getPrice() {
        return Price;
    }


}
