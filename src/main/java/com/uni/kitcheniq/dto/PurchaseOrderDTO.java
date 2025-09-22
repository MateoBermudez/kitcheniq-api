package com.uni.kitcheniq.dto;

import com.uni.kitcheniq.enums.PurchaseOrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderDTO {

    private long orderId;
    private String supplierId;
    private PurchaseOrderType status;
    private double totalAmount;
    private Set<PurchaseOrderItemDTO> items;
    private LocalDate orderDate;
    private LocalDate updateDate;


}
