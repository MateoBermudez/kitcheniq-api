package com.uni.kitcheniq.controller;

import com.uni.kitcheniq.dto.AddItemDTO;
import com.uni.kitcheniq.dto.CreateOrderDTO;
import com.uni.kitcheniq.dto.DailyEarningsDTO;
import com.uni.kitcheniq.dto.OrderResponseDTO;
import com.uni.kitcheniq.enums.OrderStatusType;
import com.uni.kitcheniq.models.*;
import com.uni.kitcheniq.repository.MenuProductRepository;
import com.uni.kitcheniq.repository.OrderStatusRepository;
import com.uni.kitcheniq.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("kitcheniq/api/v1")
public class OrderController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private MenuProductRepository menuProductRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(
                orderService.getAllOrders().stream()
                        .map(this::convertToOrderResponseDTO)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(convertToOrderResponseDTO(order));
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderDTO dto) {
        Order created = orderService.createOrderWithInitialItem(
                dto.getEmployeeId(),
                dto.getMenuComponentId(),
                dto.getQuantity(),
                dto.getTableNumber()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToOrderResponseDTO(created));
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @RequestBody OrderResponseDTO orderDTO) {
        Order existing = orderService.getOrderById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        existing.setOrderBill(orderDTO.getOrderBill());
        existing.setOrderPrice(orderDTO.getTotalPrice());
        if (orderDTO.getTableNumber() != null) existing.setTableNumber(orderDTO.getTableNumber());
        Order updated = orderService.updateOrder(id, existing);
        return ResponseEntity.ok(convertToOrderResponseDTO(updated));
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id) {
        Order cancelled = orderService.cancelOrder(id);
        if (cancelled == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(convertToOrderResponseDTO(cancelled));
    }

    @GetMapping("/order/{id}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getAllOrderItemsByOrderId(id));
    }

    @PostMapping("/order/{id}/items")
    public ResponseEntity<OrderResponseDTO> addItemToOrder(@PathVariable Long id, @RequestBody AddItemDTO itemDTO) {
        Order existingOrder = orderService.getOrderById(id);
        if (existingOrder == null) return ResponseEntity.notFound().build();
        MenuProduct product = menuProductRepository.findById(itemDTO.getMenuComponentId()).orElse(null);
        if (product == null) return ResponseEntity.badRequest().build();
        OrderItem newItem = new OrderItem();
        newItem.setOrder(existingOrder);
        newItem.setComponent(product);
        newItem.setQuantity(itemDTO.getQuantity() != null ? itemDTO.getQuantity() : 1);
        orderService.createOrderItem(newItem);
        return ResponseEntity.ok(convertToOrderResponseDTO(orderService.getOrderById(id)));
    }

    @DeleteMapping("/order/{orderId}/items/{itemId}")
    public ResponseEntity<OrderResponseDTO> removeItemFromOrder(@PathVariable Long orderId, @PathVariable Long itemId) {
        orderService.deleteOrderItemByOrderId(orderId, itemId);
        Order updated = orderService.getOrderById(orderId);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(convertToOrderResponseDTO(updated));
    }

    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(@PathVariable OrderStatusType status) {
        return ResponseEntity.ok(
                orderService.getOrdersByStatus(status).stream()
                        .map(this::convertToOrderResponseDTO)
                        .collect(Collectors.toList())
        );
    }

    @PutMapping("/order/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatusType status) {
        var updatedStatus = orderService.updateOrderStatus(id, status);
        if (updatedStatus == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(convertToOrderResponseDTO(orderService.getOrderById(id)));
    }

    @GetMapping("/orders/employee/{employeeId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByEmployee(@PathVariable String employeeId) {
        return ResponseEntity.ok(
                orderService.getOrdersByEmployeeId(employeeId).stream()
                        .map(this::convertToOrderResponseDTO)
                        .collect(Collectors.toList())
        );
    }

    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        if (order == null) return null;
        var status = orderStatusRepository.findStatusByOrders_Id(order.getId())
                .map(OrderStatus::getStatus)
                .orElse(null);
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setTotalPrice(order.getOrderPrice());
        dto.setOrderBill(order.getOrderBill());
        dto.setOrderDate(order.getOrderDate() != null ? order.getOrderDate().toString() : null);
        dto.setOrderStatus(status);
        dto.setTableNumber(order.getTableNumber());
        dto.setRequestTime(order.getRequestTime() != null ? order.getRequestTime().toString() : null);
        dto.setDeliverTime(order.getDeliverTime() != null ? order.getDeliverTime().toString() : null);
        return dto;
    }


    @GetMapping("/daily-earnings")
    public ResponseEntity<DailyEarningsDTO> getDailyEarnings(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        double totalEarnings = orderService.getDailyEarnings(date);
        DailyEarningsDTO response = new DailyEarningsDTO(date, totalEarnings);
        return ResponseEntity.ok(response);
    }


}
