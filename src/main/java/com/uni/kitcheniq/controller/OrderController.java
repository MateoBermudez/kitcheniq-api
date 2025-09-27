package com.uni.kitcheniq.controller;

import com.uni.kitcheniq.dto.AddItemDTO;
import com.uni.kitcheniq.dto.CreateOrderDTO;
import com.uni.kitcheniq.dto.OrderResponseDTO;
import com.uni.kitcheniq.enums.OrderStatusType;
import com.uni.kitcheniq.models.MenuComponent;
import com.uni.kitcheniq.models.MenuProduct;
import com.uni.kitcheniq.models.Order;
import com.uni.kitcheniq.models.OrderItem;
import com.uni.kitcheniq.models.OrderStatus;
import com.uni.kitcheniq.repository.MenuProductRepository;
import com.uni.kitcheniq.repository.OrderStatusRepository;
import com.uni.kitcheniq.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponseDTO> response = orders.stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToOrderResponseDTO(order));
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        Order createdOrder = orderService.createOrderWithInitialItem(
                createOrderDTO.getEmployeeId(),
                createOrderDTO.getMenuComponentId(),
                createOrderDTO.getQuantity());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToOrderResponseDTO(createdOrder));
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @RequestBody OrderResponseDTO orderDTO) {
        Order existingOrder = orderService.getOrderById(id);
        if (existingOrder == null) {
            return ResponseEntity.notFound().build();
        }

        existingOrder.setOrderBill(orderDTO.getOrderBill());
        existingOrder.setOrderPrice(orderDTO.getTotalPrice());

        Order updatedOrder = orderService.updateOrder(id, existingOrder);
        return ResponseEntity.ok(convertToOrderResponseDTO(updatedOrder));
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id) {
        Order cancelledOrder = orderService.cancelOrder(id);
        if (cancelledOrder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToOrderResponseDTO(cancelledOrder));
    }

    @GetMapping("/order/{id}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Long id) {
        List<OrderItem> items = orderService.getAllOrderItemsByOrderId(id);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/order/{id}/items")
    public ResponseEntity<OrderResponseDTO> addItemToOrder(@PathVariable Long id, @RequestBody AddItemDTO itemDTO) {
        Order existingOrder = orderService.getOrderById(id);
        if (existingOrder == null) {
            return ResponseEntity.notFound().build();
        }

        // Buscar el producto del menú
        MenuProduct product = menuProductRepository.findById(itemDTO.getMenuComponentId())
                .orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        // Crear el item de orden
        OrderItem newItem = new OrderItem();
        newItem.setOrder(existingOrder);
        newItem.setComponent(product);
        newItem.setQuantity(itemDTO.getQuantity() != null ? itemDTO.getQuantity() : 1);

        // Guardar el item
        orderService.createOrderItem(newItem);

        // Devolver la orden actualizada
        Order updatedOrder = orderService.getOrderById(id);
        return ResponseEntity.ok(convertToOrderResponseDTO(updatedOrder));
    }

    @DeleteMapping("/order/{orderId}/items/{itemId}")
    public ResponseEntity<OrderResponseDTO> removeItemFromOrder(
            @PathVariable Long orderId, @PathVariable Long itemId) {
        orderService.deleteOrderItemByOrderId(orderId, itemId);
        Order updatedOrder = orderService.getOrderById(orderId);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToOrderResponseDTO(updatedOrder));
    }

    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(
            @PathVariable OrderStatusType status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        List<OrderResponseDTO> response = orders.stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/order/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id, @RequestParam OrderStatusType status) {
        OrderStatus updatedStatus = orderService.updateOrderStatus(id, status);
        if (updatedStatus == null) {
            return ResponseEntity.badRequest().build();
        }

        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(convertToOrderResponseDTO(order));
    }

    @GetMapping("/orders/employee/{employeeId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByEmployee(@PathVariable String employeeId) {
        List<Order> orders = orderService.getOrdersByEmployeeId(employeeId);
        List<OrderResponseDTO> response = orders.stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Método auxiliar para convertir Order a OrderResponseDTO
    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        if (order == null) return null;

        OrderStatusType status = orderStatusRepository.findStatusByOrders_Id(order.getId())
                .map(OrderStatus::getStatus)
                .orElse(null);

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setTotalPrice(order.getOrderPrice());
        dto.setOrderBill(order.getOrderBill());
        dto.setOrderDate(order.getOrderDate().toString());
        dto.setOrderStatus(status);

        return dto;
    }
}
