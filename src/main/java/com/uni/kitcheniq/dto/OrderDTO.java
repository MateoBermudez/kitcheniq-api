package com.uni.kitcheniq.dto;

import com.uni.kitcheniq.enums.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderDTO {
    private long orderId;
    private double totalPrice;
    private String orderBill;
    private LocalDateTime orderDate;

    private OrderStatusType orderStatus;

}
