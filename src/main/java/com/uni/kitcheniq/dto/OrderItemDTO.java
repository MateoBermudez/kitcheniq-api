package com.uni.kitcheniq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderItemDTO {
    private OrderDTO order;
    private long menuItemId;
    private String menuItemName;

    Set<menuProductInventoryItemDTO> ingredients;
}
