package com.uni.kitcheniq.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}