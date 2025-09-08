package com.uni.kitcheniq.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MenuComponent {
    @Id
    @Column(name = "Id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    public abstract double getPrice();

    public Long getId(){
        return Id;
    }

    public MenuComponent() {
        // Default constructor
    }
}
