package com.uni.kitcheniq.service;

import com.uni.kitcheniq.dto.OrderResponse;
import com.uni.kitcheniq.mapper.OrderResponseMapper;
import com.uni.kitcheniq.model.Inventory;
import com.uni.kitcheniq.model.Order;
import com.uni.kitcheniq.model.OrderComponent;
import com.uni.kitcheniq.model.ProductInventory;
import com.uni.kitcheniq.repository.InventoryRepository;
import com.uni.kitcheniq.repository.OrderComponentRepository;
import com.uni.kitcheniq.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;
    private final OrderComponentRepository orderComponentRepository;

    @Autowired
    public OrderService(InventoryRepository inventoryRepository, OrderRepository orderRepository, OrderComponentRepository orderComponentRepository) {
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
        this.orderComponentRepository = orderComponentRepository;
    }

    @Transactional
    public OrderResponse createOrder(Order order) {
        order.setComponents(order.getComponents().stream()
                .map(orderComponent -> orderComponentRepository.findById(orderComponent.getId())
                        .orElseThrow(() -> new RuntimeException("Order component not found: " + orderComponent.getId())))
                .toList());

        Map<String, Integer> requirements = order.getComponents().stream()
                .flatMap(item -> item.getRequiredIngredients().stream())
                .collect(Collectors.toMap(
                        req -> req.getInventory().getName(),
                        ProductInventory::getQuantity,
                        Integer::sum
                ));

        requirements.forEach((productName, quantity) -> {
            Inventory inv = inventoryRepository.findByProductName(productName)
                    .orElseThrow(() -> new RuntimeException("No inventory found for product: " + productName));
            inv.deductQuantity(quantity);
            inventoryRepository.save(inv);
        });

        double totalPrice = order.getComponents().stream()
                .mapToDouble(OrderComponent::getPrice)
                .sum();

        order.setPrice(totalPrice);
        order.setBill("Order Bill: " + order.getDetails() + " | Total Price: $" + totalPrice);

        return OrderResponseMapper.toOrderResponse(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return OrderResponseMapper.toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponseMapper::toOrderResponse)
                .toList();
    }

    @Transactional
    public OrderResponse updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Update existing order details
        existingOrder.setDetails(order.getDetails());
        existingOrder.setStatus(order.getStatus());
        existingOrder.setComponents(order.getComponents().stream()
                .map(orderComponent -> orderComponentRepository.findById(orderComponent.getId())
                        .orElseThrow(() -> new RuntimeException("Order component not found: " + orderComponent.getId())))
                .toList());

        // Recalculate total price and bill
        double totalPrice = existingOrder.getComponents().stream()
                .mapToDouble(OrderComponent::getPrice)
                .sum();
        existingOrder.setPrice(totalPrice);
        existingOrder.setBill("Updated Order Bill: " + existingOrder.getDetails() + " | Total Price: $" + totalPrice);

        return OrderResponseMapper.toOrderResponse(orderRepository.save(existingOrder));
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Restore inventory quantities
        Map<String, Integer> requirements = order.getComponents().stream()
                .flatMap(item -> item.getRequiredIngredients().stream())
                .collect(Collectors.toMap(
                        req -> req.getInventory().getName(),
                        ProductInventory::getQuantity,
                        Integer::sum
                ));

        requirements.forEach((productName, quantity) -> {
            Inventory inv = inventoryRepository.findByProductName(productName)
                    .orElseThrow(() -> new RuntimeException("No inventory found for product: " + productName));
            inv.addQuantity(quantity);
            inventoryRepository.save(inv);
        });

        orderRepository.delete(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(String status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream()
                .map(OrderResponseMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setStatus(status);
        return OrderResponseMapper.toOrderResponse(orderRepository.save(order));
    }
}
