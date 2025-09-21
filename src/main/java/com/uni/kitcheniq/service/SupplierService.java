package com.uni.kitcheniq.service;

import com.uni.kitcheniq.dto.PurchaseOrderDTO;
import com.uni.kitcheniq.dto.PurchaseOrderItemDTO;
import com.uni.kitcheniq.enums.PurchaseOrderType;
import com.uni.kitcheniq.mapper.PurchaseOrderMapper;
import com.uni.kitcheniq.models.PurchaseOrder;
import com.uni.kitcheniq.models.PurchaseOrderItem;
import com.uni.kitcheniq.repository.InventoryItemRepository;
import com.uni.kitcheniq.repository.PurchaseOrderItemRepository;
import com.uni.kitcheniq.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public PurchaseOrderDTO getPurchaseOrder(Long orderId) {
        PurchaseOrder order = purchaseOrderRepository.findPurchaseOrderByIdWithItems(orderId);
        if (order == null) {
            return null;
        }
        order.setItems(purchaseOrderItemRepository.getItemsByOrderId(orderId));
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDTO(order);
        return purchaseOrderDTO;
    }

    @Transactional
    public String updateInventoryItem(PurchaseOrderItemDTO item){
        inventoryItemRepository.updateItemQuantity(item.getItemId(), item.getQuantity());
        return "Inventory updated successfully.";
    }

    @Transactional
    public String updateStatus(PurchaseOrderType status, Long orderId) {
        purchaseOrderRepository.updateStatusById(orderId, status);
        purchaseOrderRepository.updateTimestampById(orderId, java.time.LocalDateTime.now());

        return ("The purchase order has been updated to status: " + status + "");
    }

    @Transactional
    public double adjustInOrder(PurchaseOrderItemDTO item) {
        PurchaseOrderItem bdItem = purchaseOrderItemRepository.getItemById(item.getItemId(), item.getOrderId());

        if (bdItem.getQuantity() > item.getQuantity()) {
            purchaseOrderItemRepository.updateItemQuantity(item.getItemId(), item.getOrderId(), item.getQuantity());
            double oldSubtotalPrice = bdItem.getQuantity() * bdItem.getUnitPrice();
            double newSubtotalPrice = item.getQuantity() * item.getUnitPrice();
            purchaseOrderItemRepository.updateSubtotalInItemOfOrderId(item.getItemId(), item.getOrderId(), newSubtotalPrice);

            double difference = oldSubtotalPrice - newSubtotalPrice;
            purchaseOrderRepository.updateTotalPriceById(item.getOrderId(), difference);
        }

        return purchaseOrderRepository.getTotalPriceById(item.getOrderId());
    }

    public List<PurchaseOrderDTO> getOrders (String supplierId) {
        List<PurchaseOrder> orders = purchaseOrderRepository.getPurchaseOrderBySupplierId(supplierId);
        List<PurchaseOrderDTO> orderDTOS = new ArrayList<>();

        for (PurchaseOrder order : orders) {
            orderDTOS.add(purchaseOrderMapper.toDTO(order));
        }
        return orderDTOS;
    }
}
