package com.uni.kitcheniq.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_bill", nullable = false)
    private String orderBill;

    @Column(name = "order_details", nullable = false)
    private String orderDetails;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "order_price", nullable = false)
    private Double orderPrice;

}