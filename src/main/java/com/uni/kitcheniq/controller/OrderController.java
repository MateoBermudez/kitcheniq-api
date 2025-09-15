package com.uni.kitcheniq.controller;

import com.uni.kitcheniq.enums.OrderStatusType;
import com.uni.kitcheniq.models.Order;
import com.uni.kitcheniq.models.OrderItem;
import com.uni.kitcheniq.models.OrderStatus;
import com.uni.kitcheniq.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("kitcheniq/api/v1/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Order>> getOrdersByEmployeeId(@PathVariable String employeeId) {
        return ResponseEntity.ok(orderService.getOrdersByEmployeeId(employeeId));
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getAllOrderItemsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getAllOrderItemsByOrderId(orderId));
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) {
        return ResponseEntity.ok(orderService.createOrderItem(orderItem));
    }

    @PutMapping("/{orderId}/items")
    public ResponseEntity<OrderItem> updateOrderItemByOrderId(@PathVariable Long orderId, @RequestBody OrderItem orderItem) {
        return ResponseEntity.ok(orderService.updateOrderItemByOrderId(orderId, orderItem));
    }

    @DeleteMapping("/{orderId}/items/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItemByOrderId(@PathVariable Long orderId, @PathVariable Long orderItemId) {
        orderService.deleteOrderItemByOrderId(orderId, orderItemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable OrderStatusType status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderStatus> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusType status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }
}
