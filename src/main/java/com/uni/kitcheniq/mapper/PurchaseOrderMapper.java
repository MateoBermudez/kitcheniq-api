package com.uni.kitcheniq.mapper;

import com.uni.kitcheniq.dto.PurchaseOrderDTO;
import com.uni.kitcheniq.enums.PurchaseOrderType;
import com.uni.kitcheniq.models.PurchaseOrder;
import com.uni.kitcheniq.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PurchaseOrderMapper {

    private final PurchaseOrderItemMapper purchaseOrderItemMapper;
    private final SupplierRepository supplierRepository;

    @Autowired
    public PurchaseOrderMapper(PurchaseOrderItemMapper purchaseOrderItemMapper, SupplierRepository supplierRepository) {
        this.purchaseOrderItemMapper = purchaseOrderItemMapper;
        this.supplierRepository = supplierRepository;
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

    public PurchaseOrder toEntity(PurchaseOrderDTO purchaseOrderDTO) {
        if (purchaseOrderDTO == null) {
            return null;
        }
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .supplier(supplierRepository.findById(purchaseOrderDTO.getSupplierId()).orElse(null))
                .status(purchaseOrderDTO.getStatus())
                .totalAmount(purchaseOrderDTO.getTotalAmount())
                .updatedAt(java.time.LocalDateTime.now())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        purchaseOrder = (purchaseOrderItemMapper.toEntitySet(purchaseOrderDTO.getItems(), purchaseOrder));
        return purchaseOrder;
    }
}
