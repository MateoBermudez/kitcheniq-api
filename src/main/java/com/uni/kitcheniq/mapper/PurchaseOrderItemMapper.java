package com.uni.kitcheniq.mapper;

import com.uni.kitcheniq.dto.PurchaseOrderItemDTO;
import com.uni.kitcheniq.models.PurchaseOrder;
import com.uni.kitcheniq.models.PurchaseOrderItem;
import com.uni.kitcheniq.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class PurchaseOrderItemMapper {

    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
    public PurchaseOrderItemMapper(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public PurchaseOrderItemDTO toDTO(PurchaseOrderItem purchaseOrderItem) {
        if (purchaseOrderItem == null) {
            return null;
        }
        return PurchaseOrderItemDTO.builder()
                .OrderId(purchaseOrderItem.getPurchaseOrder().getId())
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

    public PurchaseOrderItem toEntity(PurchaseOrderItemDTO purchaseOrderItem) {
        if (purchaseOrderItem == null) {
            return null;
        }
        return PurchaseOrderItem.builder()
                .quantity(purchaseOrderItem.getQuantity())
                .unitPrice(purchaseOrderItem.getUnitPrice())
                .subTotalPrice(purchaseOrderItem.getSubTotal())
                .inventoryItem(inventoryItemRepository.findById(purchaseOrderItem.getItemId()))
                .build();
    }

    public PurchaseOrder toEntitySet(Set<PurchaseOrderItemDTO> purchaseOrderItemDTOS, PurchaseOrder purchaseOrder) {
        for (PurchaseOrderItemDTO purchaseOrderItemDTO : purchaseOrderItemDTOS){
            purchaseOrder.addItem(toEntity(purchaseOrderItemDTO));
        }
        return purchaseOrder;
    }

}
