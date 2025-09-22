package com.uni.kitcheniq.repository;

import com.uni.kitcheniq.models.InventoryItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE InventoryItem i SET i.quantity = i.quantity + :newQuantity WHERE i.id = :itemId")
    void updateItemQuantity(long itemId, int newQuantity);
}
