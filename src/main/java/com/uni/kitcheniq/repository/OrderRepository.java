package com.uni.kitcheniq.repository;

import com.uni.kitcheniq.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    //listar ordenes por id de empleado
    //List<Order> findByEmployeeId(Long employeeId);

    List<Order> findByOrderDate(LocalDate orderDate);

}
