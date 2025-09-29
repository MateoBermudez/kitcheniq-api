package com.uni.kitcheniq.service;

import com.uni.kitcheniq.dto.UserDTO;
import com.uni.kitcheniq.exception.InvalidCredentialsException;
import com.uni.kitcheniq.mapper.UserMapper;
import com.uni.kitcheniq.models.Employee;
import com.uni.kitcheniq.models.Supplier;
import com.uni.kitcheniq.repository.EmployeeRepository;
import com.uni.kitcheniq.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final EmployeeRepository employeeRepository;
    private final SupplierRepository supplierRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(EmployeeRepository employeeRepository, SupplierRepository supplierRepository, UserMapper userMapper) {
        this.employeeRepository = employeeRepository;
        this.supplierRepository = supplierRepository;
        this.userMapper = userMapper;
    }

    public UserDTO getUserbyId (String id) {
        UserDTO userDTO;

        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()){
            Employee emp = employee.get();
            userDTO = userMapper.fromEmployeeToUserDTO(emp);
        } else {
            Optional<Supplier> supplier = supplierRepository.findById(id);
            if (supplier.isPresent()) {
                Supplier sup = supplier.get();
                userDTO = userMapper.formSupplierToUserDTO(sup);
            } else {
                throw new InvalidCredentialsException("User not found");
            }
        }
        return userDTO;
    }
}
