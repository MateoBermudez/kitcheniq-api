// src/main/java/com/uni/kitcheniq/service/OrderService.java
package com.uni.kitcheniq.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.kitcheniq.enums.OrderStatusType;
import com.uni.kitcheniq.models.*;
import com.uni.kitcheniq.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MenuProductRepository menuProductRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order createOrder(Order order) {
        if (order.getRequestTime() == null) order.setRequestTime(OffsetDateTime.now());
        if (order.getTableNumber() == null) order.setTableNumber(0);
        Order savedOrder = orderRepository.save(order);
        OrderStatus initialStatus = new OrderStatus();
        initialStatus.setStatus(OrderStatusType.PENDING);
        initialStatus.setOrders(savedOrder);
        initialStatus.setId(savedOrder.getId());
        orderStatusRepository.save(initialStatus);
        return savedOrder;
    }

    @Transactional
    @Override
    public Order createOrderWithInitialItem(String employeeId, Long menuComponentId, Integer quantity, Integer tableNumber) {
        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setOrderPrice(0.0);
        order.setOrderBill("[]");
        order.setOrderDetails("");
        order.setTableNumber(tableNumber != null ? tableNumber : 0);
        order.setRequestTime(OffsetDateTime.now());
        order = orderRepository.saveAndFlush(order);

        OrderStatus initialStatus = new OrderStatus();
        initialStatus.setId(order.getId());
        initialStatus.setOrders(order);
        initialStatus.setStatus(OrderStatusType.PENDING);
        if (employeeId != null && !employeeId.isEmpty()) {
            employeeRepository.findById(employeeId).ifPresent(initialStatus::setEmployeeId);
        }
        orderStatusRepository.saveAndFlush(initialStatus);

        if (menuComponentId != null) {
            MenuProduct menuProduct = menuProductRepository.findById(menuComponentId)
                    .orElseThrow(() -> new IllegalArgumentException("MenuComponent no encontrado: " + menuComponentId));
            OrderItem initialItem = new OrderItem();
            initialItem.setOrder(order);
            initialItem.setComponent(menuProduct);
            initialItem.setQuantity(quantity != null ? quantity : 1);
            orderItemRepository.saveAndFlush(initialItem);
            double totalPrice = menuProduct.getPrice() * initialItem.getQuantity();
            order.setOrderPrice(totalPrice);
            order.setOrderBill(createSimplifiedOrderItemsJson(order.getId()));
            orderRepository.save(order);
        }
        return order;
    }

    // Ajustado: se elimina el campo "id" del ítem
    private String createSimplifiedOrderItemsJson(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        List<Map<String, Object>> simplifiedItems = new ArrayList<>();
        for (OrderItem item : items) {
            Map<String, Object> m = new HashMap<>();
            m.put("quantity", item.getQuantity());
            if (item.getComponent() != null) {
                Long cid = item.getComponent().getId();
                m.put("productId", cid);
                menuProductRepository.findById(cid).ifPresent(prod -> {
                    m.put("productName", prod.getName());
                    m.put("productPrice", prod.getPrice());
                });
            }
            simplifiedItems.add(m);
        }
        try {
            return new ObjectMapper().writeValueAsString(simplifiedItems);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        Order order = orderRepository.findById(orderItem.getOrder().getId())
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));
        OrderItem savedItem = orderItemRepository.save(orderItem);
        List<OrderItem> allItems = orderItemRepository.findByOrderId(order.getId());
        double totalPrice = 0.0;
        for (OrderItem item : allItems) {
            double price = menuProductRepository.findById(item.getComponent().getId())
                    .map(MenuProduct::getPrice).orElse(0.0);
            totalPrice += price * item.getQuantity();
        }
        order.setOrderPrice(totalPrice);
        order.setOrderBill(createSimplifiedOrderItemsJson(order.getId()));
        orderRepository.save(order);
        return savedItem;
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        Order existing = orderRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setOrderBill(order.getOrderBill());
            existing.setOrderPrice(order.getOrderPrice());
            if (order.getTableNumber() != null) existing.setTableNumber(order.getTableNumber());
            return orderRepository.save(existing);
        }
        return null;
    }

    @Override
    public Order cancelOrder(Long id) {
        Order existing = orderRepository.findById(id).orElse(null);
        if (existing != null) {
            updateOrderStatus(id, OrderStatusType.CANCELLED);
            return existing;
        }
        return null;
    }

    @Override
    public List<Order> getOrdersByEmployeeId(String employeeId) {
        List<OrderStatus> statuses = orderStatusRepository.findByEmployeeId_Id(employeeId);
        if (statuses == null || statuses.isEmpty()) return List.of();
        List<Long> ids = new ArrayList<>();
        for (OrderStatus os : statuses) {
            if (os != null && os.getId() != null && !ids.contains(os.getId())) ids.add(os.getId());
        }
        if (ids.isEmpty()) return List.of();
        return orderRepository.findAllById(ids);
    }

    @Override
    public List<OrderItem> getAllOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Override
    public OrderItem updateOrderItemByOrderId(Long orderId, OrderItem orderItem) {
        List<OrderItem> existing = orderItemRepository.findByOrderId(orderId);
        if (existing != null) {
            for (OrderItem it : existing) {
                if (it.getId().equals(orderItem.getId())) {
                    it.setComponent(orderItem.getComponent());
                    return orderItemRepository.save(it);
                }
            }
        }
        return null;
    }

    @Override
    public void deleteOrderItemByOrderId(Long orderId, Long productId) {
        List<OrderItem> existing = orderItemRepository.findByOrderId(orderId);
        if (existing == null || existing.isEmpty()) return;
        OrderItem target = null;
        for (OrderItem it : existing) {
            if (it.getComponent() != null && it.getComponent().getId().equals(productId)) {
                target = it;
                break;
            }
        }
        if (target != null) {
            if (target.getQuantity() > 1) {
                target.setQuantity(target.getQuantity() - 1);
                orderItemRepository.save(target);
            } else {
                orderItemRepository.delete(target);
            }
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null) {
                List<OrderItem> remaining = orderItemRepository.findByOrderId(orderId);
                double total = 0.0;
                for (OrderItem it : remaining) {
                    double price = menuProductRepository.findById(it.getComponent().getId())
                            .map(MenuProduct::getPrice).orElse(0.0);
                    total += price * it.getQuantity();
                }
                order.setOrderPrice(total);
                order.setOrderBill(createSimplifiedOrderItemsJson(order.getId()));
                orderRepository.save(order);
            }
        }
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatusType statusType) {
        if (statusType == null) return List.of();
        List<OrderStatus> statuses = orderStatusRepository.findAll();
        if (statuses == null || statuses.isEmpty()) return List.of();
        List<Long> orderIds = statuses.stream()
                .filter(s -> s != null && statusType.equals(s.getStatus()))
                .map(OrderStatus::getId)
                .distinct()
                .toList();
        if (orderIds.isEmpty()) return List.of();
        return orderRepository.findAllById(orderIds);
    }

    @Override
    public OrderStatus updateOrderStatus(Long orderId, OrderStatusType status) {
        if (orderId == null || status == null) return null;
        Optional<OrderStatus> lastStatus = orderStatusRepository.findStatusByOrders_Id(orderId);
        OrderStatusType current = lastStatus.map(OrderStatus::getStatus).orElse(null);

        if (status == OrderStatusType.CANCELLED) {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order == null) return null;
            OrderStatus cancelled = lastStatus.orElse(new OrderStatus());
            cancelled.setId(orderId);
            cancelled.setOrders(order);
            cancelled.setStatus(status);
            return orderStatusRepository.save(cancelled);
        }

        if (current == null) {
            if (status != OrderStatusType.PENDING) return null;
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order == null) return null;
            OrderStatus s = new OrderStatus();
            s.setId(orderId);
            s.setOrders(order);
            s.setStatus(status);
            return orderStatusRepository.save(s);
        }

        if (status == current) return lastStatus.orElse(null);

        Map<OrderStatusType, OrderStatusType> validNext = Map.of(
                OrderStatusType.PENDING, OrderStatusType.IN_PROGRESS,
                OrderStatusType.IN_PROGRESS, OrderStatusType.COMPLETED,
                OrderStatusType.COMPLETED, OrderStatusType.SERVED
        );

        OrderStatusType expectedNext = validNext.get(current);
        if (expectedNext != null && expectedNext == status) {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order == null) return null;
            OrderStatus newStatus = lastStatus.orElse(new OrderStatus());
            newStatus.setId(orderId);
            newStatus.setOrders(order);
            newStatus.setStatus(status);
            if (status == OrderStatusType.SERVED && order.getDeliverTime() == null) {
                order.setDeliverTime(OffsetDateTime.now());
                orderRepository.save(order);
            }
            return orderStatusRepository.save(newStatus);
        }
        return null;
    }

    @Override
    public double getDailyEarnings(LocalDate date) {
        List<Order> orders = orderRepository.findByOrderDate(date);

        return orders.stream()
                .filter(order -> {
                    // Excluir órdenes canceladas del cálculo de ganancias
                    return orderStatusRepository.findStatusByOrders_Id(order.getId())
                            .map(status -> status.getStatus() != OrderStatusType.CANCELLED)
                            .orElse(true); // Si no tiene estado, se asume que no está cancelada
                })
                .mapToDouble(Order::getOrderPrice)
                .sum();
    }
}
