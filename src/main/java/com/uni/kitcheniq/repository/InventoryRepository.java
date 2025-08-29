package com.uni.kitcheniq.repository;

import com.uni.kitcheniq.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query("SELECT i FROM inventory i WHERE i.name = ?1")
    Optional<Inventory> findByProductName(String productName);
}
