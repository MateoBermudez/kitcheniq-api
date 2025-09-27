package com.uni.kitcheniq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.uni.kitcheniq.enums.OrderStatusType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private Double totalPrice;
    private String orderBill;
    private String orderDate;
    private OrderStatusType orderStatus;
}
