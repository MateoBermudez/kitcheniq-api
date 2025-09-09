package com.uni.kitcheniq.models;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_component")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MenuComponent {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //TODO [Reverse Engineering] generate columns from DB
}