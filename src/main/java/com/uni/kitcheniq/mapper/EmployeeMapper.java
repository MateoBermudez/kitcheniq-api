package com.uni.kitcheniq.mapper;

import com.uni.kitcheniq.dto.EmployeeDTO;
import com.uni.kitcheniq.models.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeDTO toEmployeeDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .employeeType(employee.getType())
                .build();
    }
}
