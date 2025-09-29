package com.uni.kitcheniq.mapper;

import com.uni.kitcheniq.dto.UserDTO;
import com.uni.kitcheniq.models.Employee;
import com.uni.kitcheniq.models.Supplier;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO fromEmployeeToUserDTO (Employee employee) {
        return UserDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .entityType("Employee")
                .employeeType(employee.getType())
                .build();
    }

    public UserDTO formSupplierToUserDTO (Supplier supplier) {
        return UserDTO.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .entityType("Supplier")
                .contactInfo(supplier.getContactInfo())
                .build();
    }
}
