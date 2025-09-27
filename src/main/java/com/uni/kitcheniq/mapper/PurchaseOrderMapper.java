package com.uni.kitcheniq.mapper;

import com.uni.kitcheniq.dto.PurchaseOrderDTO;
import com.uni.kitcheniq.models.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PurchaseOrderMapper {

    private final PurchaseOrderItemMapper purchaseOrderItemMapper;

    @Autowired
    public PurchaseOrderMapper(PurchaseOrderItemMapper purchaseOrderItemMapper) {
        this.purchaseOrderItemMapper = purchaseOrderItemMapper;
    }

    public PurchaseOrderDTO toDTO(PurchaseOrder purchaseOrder) {
        if (purchaseOrder == null) {
            return null;
        }
        return PurchaseOrderDTO.builder()
                .orderId(purchaseOrder.getId())
                .supplierId(purchaseOrder.getSupplier().getId())
                .status(purchaseOrder.getStatus())
                .totalAmount(purchaseOrder.getTotalAmount())
                .orderDate(LocalDate.from(purchaseOrder.getCreatedAt()))
                .updateDate(LocalDate.from(purchaseOrder.getUpdatedAt()))
                .items(purchaseOrderItemMapper.toDTOSet(purchaseOrder.getItems()))
                .build();
    }
}
