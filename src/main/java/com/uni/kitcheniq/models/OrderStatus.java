package com.uni.kitcheniq.models;

import com.uni.kitcheniq.enums.OrderStatusType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "order_status")
public class OrderStatus {
    @Id
    @Column(name = "id_order", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_order", nullable = false)
    private Order orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employee")
    private Employee employeeId;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private OrderStatusType status;

    @Version
    @Column(name = "version")
    private Integer version;

}