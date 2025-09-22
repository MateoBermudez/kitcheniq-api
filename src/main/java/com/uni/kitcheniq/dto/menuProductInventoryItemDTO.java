package com.uni.kitcheniq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class menuProductInventoryItemDTO {
    private int quantity;
    private long inventoryItemId;
    private long menuItemId;
    private String inventoryItemName;

}
