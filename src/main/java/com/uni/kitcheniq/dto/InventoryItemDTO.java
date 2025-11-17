package com.uni.kitcheniq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryItemDTO {
    private Long id;
    private String name;
    private int quantity;
    private String supplier;
}
