package com.uni.kitcheniq.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "menu_component")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MenuComponent {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO [Reverse Engineering] generate columns from DB
}