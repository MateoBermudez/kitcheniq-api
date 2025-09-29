package com.uni.kitcheniq.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderDTO {
    private String employeeId;
    private Long menuComponentId;
    private Integer quantity;
    private Integer tableNumber; // nuevo
}
