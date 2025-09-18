package com.uni.kitcheniq.repository;

import com.uni.kitcheniq.enums.PurchaseOrderType;
import com.uni.kitcheniq.models.PurchaseOrder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE PurchaseOrder po SET po.status = ?2 WHERE po.id = ?1")
    void updateStatusById(Long id, PurchaseOrderType status);

    @Transactional
    @Modifying
    @Query("UPDATE PurchaseOrder po SET po.updatedAt = CURRENT_TIMESTAMP WHERE po.id = ?1")
    void updateTimestampById(Long id);

    @Query("SELECT po FROM PurchaseOrder po LEFT JOIN FETCH po.items WHERE po.id = :orderId")
    PurchaseOrder findPurchaseOrderByIdWithItems(Long orderId);


}
