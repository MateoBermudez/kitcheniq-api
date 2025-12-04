package com.uni.kitcheniq.mapper;

import com.uni.kitcheniq.dto.PurchaseOrderItemDTO;
import com.uni.kitcheniq.models.PurchaseOrder;
import com.uni.kitcheniq.models.PurchaseOrderItem;
import com.uni.kitcheniq.repository.InventoryItemRepository;
import com.uni.kitcheniq.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class PurchaseOrderItemMapper {

    private final InventoryItemRepository inventoryItemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    public PurchaseOrderItemMapper(InventoryItemRepository inventoryItemRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public PurchaseOrderItemDTO toDTO(PurchaseOrderItem purchaseOrderItem) {
        if (purchaseOrderItem == null) {
            return null;
        }
        return PurchaseOrderItemDTO.builder()
                .orderId(purchaseOrderItem.getPurchaseOrder().getId())
                .itemId(purchaseOrderItem.getInventoryItem().getId())
                .itemName(purchaseOrderItem.getInventoryItem().getName())
                .quantity(purchaseOrderItem.getQuantity())
                .unitPrice(purchaseOrderItem.getUnitPrice())
                .subTotal(purchaseOrderItem.getSubTotalPrice())
                .build();
    }

    public Set<PurchaseOrderItemDTO> toDTOSet(Set<PurchaseOrderItem> purchaseOrderItems) {
        if (purchaseOrderItems == null) {
            return null;
        }
        Set<PurchaseOrderItemDTO> purchaseOrderItemDTOS = new HashSet<>();
        for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {
            purchaseOrderItemDTOS.add(toDTO(purchaseOrderItem));
        }
        return purchaseOrderItemDTOS;
    }

    public PurchaseOrderItem toEntity(PurchaseOrderItemDTO purchaseOrder) {
        if (purchaseOrder == null) {
            return null;
        }

        PurchaseOrderItem purchaseOrderItem = PurchaseOrderItem.builder()
                .purchaseOrder(purchaseOrderRepository.findPurchaseOrderById(purchaseOrder.getOrderId()))
                .quantity(purchaseOrder.getQuantity())
                .unitPrice(purchaseOrder.getUnitPrice())
                .subTotalPrice(purchaseOrder.getSubTotal())
                .inventoryItem(inventoryItemRepository.findById(purchaseOrder.getItemId()))
                .build();
        return purchaseOrderItem;
    }

    public PurchaseOrder toEntitySet(Set<PurchaseOrderItemDTO> purchaseOrderItemDTOS, PurchaseOrder purchaseOrder) {
        for (PurchaseOrderItemDTO purchaseOrderItemDTO : purchaseOrderItemDTOS){
            purchaseOrder.addItem(toEntity(purchaseOrderItemDTO));
        }
        return purchaseOrder;
    }

}
