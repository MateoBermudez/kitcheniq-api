package com.uni.kitcheniq.service;

import com.uni.kitcheniq.dto.InventoryItemDTO;
import com.uni.kitcheniq.dto.PurchaseOrderDTO;
import com.uni.kitcheniq.exception.NoItemFoundException;
import com.uni.kitcheniq.exception.SupplierNotFoundException;
import com.uni.kitcheniq.mapper.InventoryItemMapper;
import com.uni.kitcheniq.mapper.PurchaseOrderMapper;
import com.uni.kitcheniq.models.InventoryItem;
import com.uni.kitcheniq.models.PurchaseOrder;
import com.uni.kitcheniq.repository.InventoryItemRepository;
import com.uni.kitcheniq.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemMapper inventoryItemMapper;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    public AdminService(InventoryItemRepository inventoryItemRepository, InventoryItemMapper inventoryItemMapper,
                        PurchaseOrderMapper purchaseOrderMapper, PurchaseOrderRepository purchaseOrderRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryItemMapper = inventoryItemMapper;
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public String addInventoryItem(InventoryItemDTO inventoryItemDTO) {
        if (inventoryItemDTO == null) {
            throw new NoItemFoundException("Inventory item data is missing");
        }
        InventoryItem item = inventoryItemMapper.toInventoryItem(inventoryItemDTO);

        if (item.getSupplierid() == null) {
            throw new SupplierNotFoundException("Supplier data is missing");
        }

        inventoryItemRepository.save(item);
        return String.format("Successfully added item: %s", item.getName());
    }

    public List<InventoryItemDTO> getAllInventoryItems() {
        List<InventoryItem> items = inventoryItemRepository.findAll();
        List<InventoryItemDTO> itemDTOs = new ArrayList<>();
        for (InventoryItem item : items) {
            itemDTOs.add(inventoryItemMapper.toInventoryItemDTO(item));
        }

        return itemDTOs;
    }

    public String createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.toEntity(purchaseOrderDTO);
        purchaseOrderRepository.save(purchaseOrder);
        return ("Hola");
    }
}
