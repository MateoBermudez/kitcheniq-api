package com.uni.kitcheniq.repository;

import com.uni.kitcheniq.models.PurchaseOrderItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem,Long> {

    @Query("SELECT poi FROM PurchaseOrderItem poi WHERE poi.purchaseOrder.id = :orderId")
    Set<PurchaseOrderItem> getItemsByOrderId(Long orderId);

    @Query("SELECT poi FROM PurchaseOrderItem poi WHERE poi.inventoryItem.id = :id AND poi.purchaseOrder.id = :orderId")
    PurchaseOrderItem getItemById(Long id, Long orderId);

    @Transactional
    @Modifying
    @Query("UPDATE PurchaseOrderItem poi SET poi.quantity = :newQuantity WHERE poi.inventoryItem.id = :itemId " +
            "AND poi.purchaseOrder.id = :orderId")
    void updateItemQuantity(Long itemId, Long orderId, int newQuantity);

    @Transactional
    @Modifying
    @Query("UPDATE PurchaseOrderItem poi SET poi.subTotalPrice = :subtotal WHERE poi.inventoryItem.id = :itemId " +
            "AND poi.purchaseOrder.id = :orderId")
    void updateSubtotalInItemOfOrderId(Long itemId, Long orderId, double subtotal);

}
