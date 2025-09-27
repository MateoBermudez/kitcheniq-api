package com.uni.kitcheniq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {
    private String employeeId;
    private Long menuComponentId;
    private Integer quantity = 1;
}
