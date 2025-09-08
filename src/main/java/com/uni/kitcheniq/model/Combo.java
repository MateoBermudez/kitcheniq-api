package com.uni.kitcheniq.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "Combo")
public class Combo extends MenuComponent{

    @Column(name = "Name", nullable = false)
    private String Name;

    @Column(name = "Price", nullable = false)
    private double Price;

    @ManyToMany
    @JoinTable(
        name = "Combo_MenuProduct",
        joinColumns = @JoinColumn(name = "ComboId"),
        inverseJoinColumns = @JoinColumn(name = "MenuProductId")
    )
    private Set<MenuProduct> Products;

    public Combo() {
        // Default constructor
    }

    @Override
    public double getPrice() {
        return Price;
    }


}
