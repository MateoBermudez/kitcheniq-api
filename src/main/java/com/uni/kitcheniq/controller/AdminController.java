package com.uni.kitcheniq.controller;

import com.uni.kitcheniq.dto.InventoryItemDTO;
import com.uni.kitcheniq.dto.PurchaseOrderDTO;
import com.uni.kitcheniq.dto.PurchaseOrderItemDTO;
import com.uni.kitcheniq.dto.SupplierDTO;
import com.uni.kitcheniq.models.PurchaseOrder;
import com.uni.kitcheniq.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("kitcheniq/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/add-inventory-item")
    public ResponseEntity<String> addInventoryItem(@RequestBody InventoryItemDTO item) {
        String response = adminService.addInventoryItem(item);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inventory-list")
    public ResponseEntity<List<InventoryItemDTO>> getInventoryItems() {
        List<InventoryItemDTO> items = adminService.getAllInventoryItems();
        return ResponseEntity.ok(items);
    }

    @PostMapping("/initialize-purchase-order")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody SupplierDTO supplierDTO) {
        PurchaseOrderDTO order = adminService.createPurchaseOrder(supplierDTO);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/add-items-to-order")
    public ResponseEntity<PurchaseOrderDTO> addItemsToPurchaseOrder(@RequestBody PurchaseOrderItemDTO purchaseOrderItemDTO) {
        PurchaseOrderDTO order = adminService.addItemsToOrder(purchaseOrderItemDTO);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/eliminate-items-from-order")
    public ResponseEntity<PurchaseOrderDTO> eliminateItemsFromOrder(@RequestBody PurchaseOrderItemDTO purchaseOrderItemDTO) {
        PurchaseOrderDTO order = adminService.eliminateItemsFromOrder(purchaseOrderItemDTO);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/cancel-purchase-order")
    public ResponseEntity<String> cancelPurchaseOrder(@RequestParam Long orderId) {
        String message = adminService.cancelPurchaseOrder(orderId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/finalize-purchase-order")
    public ResponseEntity<PurchaseOrderDTO> finalizePurchaseOrder(@RequestParam Long orderId) {
        PurchaseOrderDTO order = adminService.finalizePurchaseOrder(orderId);
        return ResponseEntity.ok(order);
    }
}
