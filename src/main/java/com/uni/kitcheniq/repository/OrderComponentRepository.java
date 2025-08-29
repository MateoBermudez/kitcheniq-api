package com.uni.kitcheniq.repository;

import com.uni.kitcheniq.model.OrderComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderComponentRepository extends JpaRepository<OrderComponent, Long> {
}
