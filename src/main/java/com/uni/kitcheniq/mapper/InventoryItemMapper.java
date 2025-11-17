package com.uni.kitcheniq.mapper;

import com.uni.kitcheniq.dto.InventoryItemDTO;
import com.uni.kitcheniq.models.InventoryItem;
import com.uni.kitcheniq.models.Supplier;
import com.uni.kitcheniq.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InventoryItemMapper {

    private final SupplierRepository supplierRepository;

    @Autowired
    public InventoryItemMapper(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public InventoryItem toInventoryItem(InventoryItemDTO inventoryItemDTO) {
        if (inventoryItemDTO == null) {
            return null;
        }

        InventoryItem item = InventoryItem.builder()
                .id(inventoryItemDTO.getId())
                .name(inventoryItemDTO.getName())
                .quantity(inventoryItemDTO.getQuantity())
                .build();

        Optional<Supplier> supplier = supplierRepository.findById(inventoryItemDTO.getSupplier());

        if (supplier.isPresent()) {
            item.setSupplierid(supplier.get());
        } else {
            item.setSupplierid(null);
        }
        return item;
    }

    public InventoryItemDTO toInventoryItemDTO(InventoryItem inventoryItem) {
        if (inventoryItem == null) {
            return null;
        }

        return InventoryItemDTO.builder()
                .id(inventoryItem.getId())
                .name(inventoryItem.getName())
                .quantity(inventoryItem.getQuantity())
                .supplier(inventoryItem.getSupplierid() != null ? inventoryItem.getSupplierid().getId().toString() : null)
                .build();
    }
}
