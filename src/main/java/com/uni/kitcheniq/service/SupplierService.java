package com.uni.kitcheniq.service;

import com.uni.kitcheniq.dto.PurchaseOrderDTO;
import com.uni.kitcheniq.dto.PurchaseOrderItemDTO;
import com.uni.kitcheniq.enums.PurchaseOrderType;
import com.uni.kitcheniq.mapper.PurchaseOrderMapper;
import com.uni.kitcheniq.models.PurchaseOrder;
import com.uni.kitcheniq.repository.InventoryItemRepository;
import com.uni.kitcheniq.repository.PurchaseOrderItemRepository;
import com.uni.kitcheniq.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    public SupplierService(PurchaseOrderRepository purchaseOrderRepository,
                           PurchaseOrderItemRepository purchaseOrderItemRepository,
                           InventoryItemRepository inventoryItemRepository, PurchaseOrderMapper purchaseOrderMapper) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.purchaseOrderMapper = purchaseOrderMapper;
    }

    @Transactional
    public String updateFirstStatus(PurchaseOrderDTO purchaseOrderDTO) {
        PurchaseOrderType statusEnum = purchaseOrderDTO.getStatus();
        String status = statusEnum.name();

        purchaseOrderRepository.updateStatusById(purchaseOrderDTO.getOrderId(), statusEnum);
        purchaseOrderRepository.updateTimestampById(purchaseOrderDTO.getOrderId());

        if (status.equals("ACCEPTED")) {
            return "Purchase order ACCEPTED correctly.";
        } else if (status.equals("REJECTED")) {
            return "Purchase order REJECTED correctly.";
        } else {
            return "Invalid status.";
        }
    }


    public PurchaseOrderDTO getPurchaseOrder(Long orderId) {
        PurchaseOrder order = purchaseOrderRepository.findPurchaseOrderByIdWithItems(orderId);
        if (order == null) {
            return null;
        }
        order.setItems(purchaseOrderItemRepository.getItemsByOrderId(orderId));
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDTO(order);
        return purchaseOrderDTO;
    }


    public String updateInventoryItem(PurchaseOrderItemDTO item){
        inventoryItemRepository.updateItemQuantity(item.getItemId(), item.getQuantity());
        return "Inventory updated successfully.";
    }

    @Transactional
    public void updateStatus(PurchaseOrderType status, Long orderId) {
        purchaseOrderRepository.updateStatusById(orderId, status);
        purchaseOrderRepository.updateTimestampById(orderId);
    }
}
