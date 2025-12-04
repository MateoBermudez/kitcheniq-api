package com.uni.kitcheniq.repository;

import com.uni.kitcheniq.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Optional<Employee> findById(String id);

    @Query("SELECT e FROM Employee e")
    Optional<List<Employee>> getAllEmployees();

}
