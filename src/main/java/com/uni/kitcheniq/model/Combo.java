package com.uni.kitcheniq.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "combo")
@DiscriminatorValue("COMBO")
public class Combo extends OrderComponent {

    @Column(name = "combo_name", nullable = false)
    private String name;

    @Column(name = "combo_price", nullable = false)
    private double price;

    @ManyToMany
    private List<OrderComponent> products = new ArrayList<>();

    @Override
    public double getPrice() {
        return products.stream().mapToDouble(OrderComponent::getPrice).sum(); // Sum of all product prices in the combo (No discount applied)
    }

    @Override
    public String getDetails() {
        price = getPrice();
        return name + " - $" + getPrice() + " (Combo)";
    }

    @Override
    public Set<ProductInventory> getRequiredIngredients() {
        Set<ProductInventory> total= new HashSet<>();
        for (OrderComponent prod : products) {
            total.addAll(prod.getRequiredIngredients());
        }
        return total;
    }

    public List<OrderComponent> getProducts() {
        return products;
    }

    public void setProducts(List<OrderComponent> products) {
        this.products = products;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}