package com.uni.kitcheniq.mapper;

import com.uni.kitcheniq.dto.EmployeeDTO;
import com.uni.kitcheniq.dto.EmployeeRequest;
import com.uni.kitcheniq.models.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeDTO toEmployeeDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .lastName(employee.getLastName())
                .employeeType(employee.getType())
                .hourlyRate(employee.getHourlyRate())
                .build();
    }

    public Employee toEntity(EmployeeRequest request) {
        return Employee.builder()
                .id(request.getId())
                .name(request.getName())
                .lastName(request.getLastName())
                .type(request.getEmployeeType())
                .hourlyRate(request.getHourlyRate())
                .build();
    }
}
