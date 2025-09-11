package com.uni.kitcheniq.dto;

import com.uni.kitcheniq.enums.EmployeeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String id;
    private String name;
    private String password;
    private String entityType; // "employee" or "supplier"

    // Supplier-specific
    private String contactInfo;

    // Employee-specific
    private EmployeeType employeeType;

    // Getters and setters
    // ...
}