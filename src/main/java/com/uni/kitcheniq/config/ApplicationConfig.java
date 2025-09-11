package com.uni.kitcheniq.config;

import com.uni.kitcheniq.models.Employee;
import com.uni.kitcheniq.models.Supplier;
import com.uni.kitcheniq.repository.EmployeeRepository;
import com.uni.kitcheniq.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final EmployeeRepository employeeRepository;
    private final SupplierRepository supplierRepository;

    @Bean
    public AuthenticationManager AuthenticationManager (AuthenticationConfiguration Config) throws Exception {

        return Config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider AuthenticationProvider () {

        DaoAuthenticationProvider AuthenticationProvider = new DaoAuthenticationProvider();
        AuthenticationProvider.setUserDetailsService(userDetailsService());
        AuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return AuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userid -> {
            Optional<Employee> employee = employeeRepository.findById(userid);
            if (employee.isPresent()) {
                return employee.get();
            }
            Optional<Supplier> supplier = supplierRepository.findById(userid);
            if (supplier.isPresent()) {
                return supplier.get();
            }
            throw new RuntimeException("User not found");
        };
    }
}
