package com.uni.kitcheniq.service;

import com.uni.kitcheniq.dto.InventoryItemDTO;
import com.uni.kitcheniq.dto.PurchaseOrderDTO;
import com.uni.kitcheniq.dto.PurchaseOrderItemDTO;
import com.uni.kitcheniq.dto.SupplierDTO;
import com.uni.kitcheniq.enums.PurchaseOrderType;
import com.uni.kitcheniq.exception.NoItemFoundException;
import com.uni.kitcheniq.exception.SupplierNotFoundException;
import com.uni.kitcheniq.mapper.InventoryItemMapper;
import com.uni.kitcheniq.mapper.PurchaseOrderItemMapper;
import com.uni.kitcheniq.mapper.PurchaseOrderMapper;
import com.uni.kitcheniq.models.InventoryItem;
import com.uni.kitcheniq.models.PurchaseOrder;
import com.uni.kitcheniq.models.PurchaseOrderItem;
import com.uni.kitcheniq.models.Supplier;
import com.uni.kitcheniq.repository.InventoryItemRepository;
import com.uni.kitcheniq.repository.PurchaseOrderItemRepository;
import com.uni.kitcheniq.repository.PurchaseOrderRepository;
import com.uni.kitcheniq.repository.SupplierRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AdminService {

    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemMapper inventoryItemMapper;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderItemMapper purchaseOrderItemMapper;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final EntityManager em;


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

    /*public String createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.toEntity(purchaseOrderDTO);
        purchaseOrderRepository.save(purchaseOrder);
        return ("Purchase order created successfully");
    }*/

    public PurchaseOrderDTO createPurchaseOrder(SupplierDTO supplierDTO) {
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .status(PurchaseOrderType.PENDING)
                .totalAmount(0.0)
                .build();

        Optional<Supplier> supplier = supplierRepository.findById(supplierDTO.getId());
        if (supplier.isPresent()) {
            purchaseOrder.setSupplier(supplier.get());
        } else {
            throw new SupplierNotFoundException("Supplier not found with ID: " + supplierDTO.getId());
        }

        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);
        PurchaseOrderDTO orderDTO = purchaseOrderMapper.toDTO(saved);

        return orderDTO;
    }

    public PurchaseOrderDTO addItemsToOrder(PurchaseOrderItemDTO itemDTO) {
        PurchaseOrderItem item = purchaseOrderItemMapper.toEntity(itemDTO);
        purchaseOrderItemRepository.save(item);
        purchaseOrderRepository.addToTotalAmountById(item.getPurchaseOrder().getId(), item.getSubTotalPrice());
        PurchaseOrder saved = purchaseOrderRepository.findPurchaseOrderByIdWithItems(item.getPurchaseOrder().getId());

        PurchaseOrderDTO orderDTO = purchaseOrderMapper.toDTO(saved);
        return orderDTO;
    }

    public PurchaseOrderDTO eliminateItemsFromOrder(PurchaseOrderItemDTO itemDTO) {
        purchaseOrderItemRepository.deleteItemFromOrder(itemDTO.getItemId(), itemDTO.getOrderId());
        purchaseOrderRepository.updateTotalPriceById(itemDTO.getOrderId(), itemDTO.getSubTotal());

        PurchaseOrder saved = purchaseOrderRepository.findPurchaseOrderByIdWithItems(itemDTO.getOrderId());
        PurchaseOrderDTO orderDTO = purchaseOrderMapper.toDTO(saved);
        return orderDTO;
    }

    @Transactional
    public PurchaseOrderDTO finalizePurchaseOrder(Long orderId) {
        PurchaseOrder order = purchaseOrderRepository.findPurchaseOrderByIdWithItems(orderId);
        if (order.getTotalAmount() == 0) {
            purchaseOrderRepository.updateStatusById(orderId, PurchaseOrderType.CANCELLED);
            em.flush();
            em.refresh(order);
        }

        PurchaseOrder saved = purchaseOrderRepository.findPurchaseOrderByIdWithItems(orderId);
        PurchaseOrderDTO orderDTO = purchaseOrderMapper.toDTO(saved);
        return orderDTO;
    }

    @Transactional
    public String cancelPurchaseOrder(Long orderId) {
        PurchaseOrder order = purchaseOrderRepository.findPurchaseOrderByIdWithItems(orderId);
        for (PurchaseOrderItem item : order.getItems()) {
            purchaseOrderItemRepository.deleteItemFromOrder(item.getId(), orderId);
        }
        purchaseOrderRepository.deletePurchaseOrderById(orderId);
        return "Purchase order cancelled successfully";
    }


}
