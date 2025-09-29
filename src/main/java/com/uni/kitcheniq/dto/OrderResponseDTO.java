package com.uni.kitcheniq.dto;

import com.uni.kitcheniq.enums.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private Double totalPrice;
    private String orderBill;
    private String orderDate;
    private OrderStatusType orderStatus;
    private Integer tableNumber;
    private String requestTime;
    private String deliverTime;
}
