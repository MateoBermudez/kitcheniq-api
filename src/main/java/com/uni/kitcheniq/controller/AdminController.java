package com.uni.kitcheniq.controller;

import com.uni.kitcheniq.dto.InventoryItemDTO;
import com.uni.kitcheniq.dto.PurchaseOrderDTO;
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

    @PostMapping("/create-purchase-order")
    public ResponseEntity<String> createPurchaseOrder(@RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        adminService.createPurchaseOrder(purchaseOrderDTO);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/inventory-search")
    public ResponseEntity<List<InventoryItemDTO>> searchInventoryItems(
            @RequestParam(name = "name", required = false) String name) {
        return ResponseEntity.ok(adminService.searchItemsByName(name));
    }
}
