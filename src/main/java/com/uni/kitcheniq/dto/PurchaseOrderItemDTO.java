package com.uni.kitcheniq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderItemDTO {
    private long OrderId;
    private long itemId;
    private int quantity;
    private double unitPrice;
    private double subTotal;

}
