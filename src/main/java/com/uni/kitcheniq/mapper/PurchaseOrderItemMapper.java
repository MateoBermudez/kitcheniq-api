package com.uni.kitcheniq.mapper;

import com.uni.kitcheniq.dto.PurchaseOrderItemDTO;
import com.uni.kitcheniq.models.PurchaseOrderItem;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class PurchaseOrderItemMapper {

    public PurchaseOrderItemDTO toDTO(PurchaseOrderItem purchaseOrderItem) {
        if (purchaseOrderItem == null) {
            return null;
        }
        return PurchaseOrderItemDTO.builder()
                .OrderId(purchaseOrderItem.getPurchaseOrder().getId())
                .itemId(purchaseOrderItem.getInventoryItem().getId())
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

}
