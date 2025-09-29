package com.uni.kitcheniq.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_bill", nullable = false)
    private String orderBill;

    @Column(name = "order_details")
    private String orderDetails;

    @Column(name = "order_date", nullable = false)
    private java.time.LocalDate orderDate;

    @Column(name = "order_price", nullable = false)
    private Double orderPrice;

    @Column(name = "table_number", nullable = false)
    private Integer tableNumber;

    @Column(name = "request_time", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime requestTime;

    @Column(name = "deliver_time", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime deliverTime;

    @PrePersist
    public void prePersist() {
        if (this.requestTime == null) {
            this.requestTime = OffsetDateTime.now();
        }
    }

}
