package com.uni.kitcheniq.dto;

import com.uni.kitcheniq.enums.EmployeeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDTO {
    private String id;
    private String name;
    private EmployeeType employeeType;
}
