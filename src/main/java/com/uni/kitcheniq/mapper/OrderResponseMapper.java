package com.uni.kitcheniq.mapper;

import com.uni.kitcheniq.dto.OrderResponse;
import com.uni.kitcheniq.model.Order;
import com.uni.kitcheniq.model.OrderComponent;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponseMapper {

    public static OrderResponse toOrderResponse(Order order) {
        if (order == null) {
            return null;
        }

        List<String> items = order.getComponents().stream()
                .map(OrderComponent::getDetails)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getDetails(),
                order.getPrice(),
                order.getBill(),
                order.getStatus(),
                order.getOrderDate(),
                items
        );
    }
}
