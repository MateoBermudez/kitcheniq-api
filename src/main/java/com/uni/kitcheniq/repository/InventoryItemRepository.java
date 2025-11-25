package com.uni.kitcheniq.repository;

import com.uni.kitcheniq.models.InventoryItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE InventoryItem i SET i.quantity = i.quantity + :newQuantity WHERE i.id = :itemId")
    void updateItemQuantity(long itemId, int newQuantity);

    InventoryItem findById(long itemId);
    List<InventoryItem> findByNameContainingIgnoreCase(String name);

    @Query("SELECT i FROM InventoryItem i WHERE i.supplierid.id = :supplierId")
    List<InventoryItem> getInventoryItemsBySupplierId(String supplierId);
}
