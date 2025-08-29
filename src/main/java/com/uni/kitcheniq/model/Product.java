package com.uni.kitcheniq.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity(name = "product")
@DiscriminatorValue("PRODUCT")
public class Product extends OrderComponent {

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "product_price", nullable = false)
    private double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ProductInventory> ingredients;

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDetails() {
        return name + " - $" + price;
    }

    @Override
    public Set<ProductInventory> getRequiredIngredients() {
        return ingredients;
    }

    public Product() {
        // Default constructor
    }

    public Product(String name, double price, Set<ProductInventory> ingredients) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    public Set<ProductInventory> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<ProductInventory> requiredElements) {
        this.ingredients = requiredElements;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
