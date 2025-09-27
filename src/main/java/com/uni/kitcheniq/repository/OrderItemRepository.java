package com.uni.kitcheniq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uni.kitcheniq.models.OrderItem;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
    void deleteById(Long id);

}
