package com.uni.kitcheniq.service;

import com.uni.kitcheniq.dto.AuthResponse;
import com.uni.kitcheniq.dto.LoginRequest;
import com.uni.kitcheniq.dto.RegisterRequest;
import com.uni.kitcheniq.models.Employee;
import com.uni.kitcheniq.models.Supplier;
import com.uni.kitcheniq.repository.EmployeeRepository;
import com.uni.kitcheniq.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final SupplierRepository supplierRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest Request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(Request.getUserId(),
                Request.getPassword()));
        UserDetails user;
        Optional<Employee> employee = employeeRepository.findById(Request.getUserId());
        if (employee.isPresent()) {
            user = employee.get();
        } else {
            Optional<Supplier> supplier = supplierRepository.findById(Request.getUserId());
            if (supplier.isPresent()) {
                user = supplier.get();
            }
            else {
                throw new RuntimeException("User not found");
            }
        }

        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .Token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest Request) {

        String userType = Request.getEntityType();
        System.out.println(userType);

        if (userType.equals("Employee")) {
            Employee employee = Employee.builder()
                    .id(Request.getId())
                    .password(passwordEncoder.encode(Request.getPassword()))
                    .name(Request.getName())
                    .type(Request.getEmployeeType())
                    .build();

            employeeRepository.save(employee);

            return AuthResponse.builder()
                    .Token(jwtService.getToken(employee))
                    .build();
        } else {
            Supplier supplier = Supplier.builder()
                    .id(Request.getId())
                    .password(passwordEncoder.encode(Request.getPassword()))
                    .name(Request.getName())
                    .contactInfo(Request.getContactInfo())
                    .build();

            supplierRepository.save(supplier);

            return AuthResponse.builder()
                    .Token(jwtService.getToken(supplier))
                    .build();
        }
    }
}
