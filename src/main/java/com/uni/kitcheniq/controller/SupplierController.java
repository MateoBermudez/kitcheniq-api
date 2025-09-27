package com.uni.kitcheniq.controller;

import com.uni.kitcheniq.dto.PurchaseOrderDTO;
import com.uni.kitcheniq.dto.PurchaseOrderItemDTO;
import com.uni.kitcheniq.enums.PurchaseOrderType;
import com.uni.kitcheniq.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("kitcheniq/api/v1/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping("/purchase-order")
    public ResponseEntity<String> receivePurchaseOrder(@RequestParam Long orderId, @RequestParam String status) {
        PurchaseOrderType purchaseOrderType = PurchaseOrderType.valueOf(status);
        String response = supplierService.updateStatus(purchaseOrderType, orderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/initiate-dispatch")
    public ResponseEntity<PurchaseOrderDTO> initiateOrderDispatch(@RequestParam Long orderId) {
        PurchaseOrderDTO processedOrder = supplierService.getPurchaseOrder(orderId);
        supplierService.updateStatus(PurchaseOrderType.DISPATCHING, orderId);
        return ResponseEntity.ok(processedOrder);
    }

    @PostMapping("/deliver-order")
    public ResponseEntity<Double> deliverOrderProducts(@RequestBody PurchaseOrderItemDTO item) {
        double subtotal = supplierService.adjustInOrder(item);
        supplierService.updateInventoryItem(item);
        return ResponseEntity.ok(subtotal);
    }

    @PostMapping("/finish-dispatch")
    public ResponseEntity<String> finishOrderDispatch(@RequestParam Long purchaseOrderDTO) {
        supplierService.updateStatus(PurchaseOrderType.DELIVERED, purchaseOrderDTO);
        return ResponseEntity.ok("Order has been delivered.");
    }

    @GetMapping("/get-orders")
    public ResponseEntity<List<PurchaseOrderDTO>> getOrders(@RequestParam String supplierId){
        List<PurchaseOrderDTO> orders = supplierService.getOrders(supplierId);
        return ResponseEntity.ok(orders);
    }
}
