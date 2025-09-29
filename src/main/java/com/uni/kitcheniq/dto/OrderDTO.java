package com.uni.kitcheniq.dto;

import com.uni.kitcheniq.enums.OrderStatusType;
import lombok.*;

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
    private Integer tableNumber;
    private String requestTime;
    private String deliverTime;
}
