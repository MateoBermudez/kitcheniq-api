package com.uni.kitcheniq.controller;

import com.uni.kitcheniq.dto.PurchaseOrderDTO;
import com.uni.kitcheniq.dto.PurchaseOrderItemDTO;
import com.uni.kitcheniq.enums.PurchaseOrderType;
import com.uni.kitcheniq.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("kitcheniq/api/v1/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping("/purchase-order")
    public ResponseEntity<String> receivePurchaseOrder(@RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        String response = supplierService.updateFirstStatus(purchaseOrderDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/initiate-dispatch")
    public ResponseEntity<PurchaseOrderDTO> initiateOrderDispatch(@RequestBody Long orderId) {
        PurchaseOrderDTO processedOrder = supplierService.getPurchaseOrder(orderId);
        supplierService.updateStatus(PurchaseOrderType.DISPATCHING, orderId);
        return ResponseEntity.ok(processedOrder);
    }

    @PostMapping("/deliver-order")
    public ResponseEntity<String> deliverOrderProducts(@RequestBody Set<PurchaseOrderItemDTO> purchaseOrderItemDTO) {
        for (PurchaseOrderItemDTO item : purchaseOrderItemDTO) {
            supplierService.updateInventoryItem(item);
        }
        supplierService.updateStatus(PurchaseOrderType.DELIVERED, purchaseOrderItemDTO.iterator()
                .next().getOrderId());
        return ResponseEntity.ok("Order delivered and inventory updated successfully.");
    }
}
