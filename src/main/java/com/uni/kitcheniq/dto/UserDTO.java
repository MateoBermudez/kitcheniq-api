package com.uni.kitcheniq.dto;

import com.uni.kitcheniq.enums.EmployeeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;
    private String name;
    private String password;
    private String entityType; // "employee" or "supplier"

    // Supplier-specific
    private String contactInfo;

    // Employee-specific
    private EmployeeType employeeType;
}
