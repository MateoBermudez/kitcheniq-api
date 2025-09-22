package com.uni.kitcheniq.models;


import com.uni.kitcheniq.enums.PurchaseOrderType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "purchase_orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    PurchaseOrderType status;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PurchaseOrderItem> items;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
