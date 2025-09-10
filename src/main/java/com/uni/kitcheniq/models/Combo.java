package com.uni.kitcheniq.models;

import jakarta.persistence.*;

@Entity
@Table(name = "combo")
public class Combo extends MenuComponent {

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private MenuComponent menuComponent;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    public MenuComponent getMenuComponent() {
        return menuComponent;
    }

    public void setMenuComponent(MenuComponent menuComponent) {
        this.menuComponent = menuComponent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}