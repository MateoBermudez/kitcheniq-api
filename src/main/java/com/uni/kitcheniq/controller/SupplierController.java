package com.uni.kitcheniq.controller;

import com.uni.kitcheniq.dto.PurchaseOrderDTO;
import com.uni.kitcheniq.dto.PurchaseOrderItemDTO;
import com.uni.kitcheniq.enums.PurchaseOrderType;
import com.uni.kitcheniq.service.PdfService;
import com.uni.kitcheniq.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("kitcheniq/api/v1/suppliers")
public class SupplierController {

    private final SupplierService supplierService;
    private final PdfService pdfService;

    @Autowired
    public SupplierController(SupplierService supplierService, PdfService pdfService) {
        this.supplierService = supplierService;
        this.pdfService = pdfService;
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
        return ResponseEntity.ok(supplierService.finishOrder(purchaseOrderDTO));
    }

    @GetMapping("/get-orders")
    public ResponseEntity<List<PurchaseOrderDTO>> getOrders(@RequestParam String supplierId){
        List<PurchaseOrderDTO> orders = supplierService.getOrders(supplierId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/get-order-pdf")
    public ResponseEntity<byte[]> getOrderPdf(@RequestParam Long orderId) {
        byte[] pdfBytes =  pdfService.generatePurchaseOrderPdf(orderId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=purchase_order_" + orderId + ".pdf")
                .body(pdfBytes);
    }
}
