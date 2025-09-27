package com.uni.kitcheniq.models;

import com.uni.kitcheniq.enums.OrderStatusType;
import jakarta.persistence.*;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrders() {
        return orders;
    }

    public void setOrders(Order orders) {
        this.orders = orders;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setIdEmployee(Employee idEmployee) {
        this.employeeId = idEmployee;
    }

    public OrderStatusType getStatus() {
        return status;
    }

    public void setStatus(OrderStatusType status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
