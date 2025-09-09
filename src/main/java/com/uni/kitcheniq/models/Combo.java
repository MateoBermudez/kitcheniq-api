package com.uni.kitcheniq.models;

import jakarta.persistence.*;

@Entity
@Table(name = "combo")
public class Combo {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private MenuComponent menuComponent;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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