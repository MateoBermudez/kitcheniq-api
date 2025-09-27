// src/main/java/com/uni/kitcheniq/repository/OrderStatusRepository.java
package com.uni.kitcheniq.repository;

import com.uni.kitcheniq.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    Optional<OrderStatus> findStatusByOrders_Id(Long orderId);
    List<OrderStatus> findByEmployeeId_Id(String employeeId);
}
