package com.uni.kitcheniq.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.kitcheniq.enums.OrderStatusType;
import com.uni.kitcheniq.models.*;
import com.uni.kitcheniq.repository.EmployeeRepository;
import com.uni.kitcheniq.repository.OrderItemRepository;
import com.uni.kitcheniq.repository.OrderRepository;
import com.uni.kitcheniq.repository.MenuProductRepository;
import com.uni.kitcheniq.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Order createOrderWithInitialItem(String employeeId, Long menuComponentId, Integer quantity) {
        // Crear orden principal
        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setOrderPrice(0.0);
        order.setOrderBill("[]");
        order.setOrderDetails("");
        order = orderRepository.saveAndFlush(order);

        // Crear estado inicial
        OrderStatus initialStatus = new OrderStatus();
        initialStatus.setId(order.getId());
        initialStatus.setOrders(order);
        initialStatus.setStatus(OrderStatusType.PENDING);

        if (employeeId != null && !employeeId.isEmpty()) {
            Employee employee = employeeRepository.findById(employeeId).orElse(null);
            if (employee != null) {
                initialStatus.setIdEmployee(employee);
            }
        }

        orderStatusRepository.saveAndFlush(initialStatus);

        // Añadir ítem si es necesario
        if (menuComponentId != null) {
            try {
                MenuProduct menuProduct = menuProductRepository.findById(menuComponentId)
                        .orElseThrow(() -> new IllegalArgumentException("MenuComponent no encontrado: " + menuComponentId));

                // Crear el OrderItem con el nuevo campo quantity
                OrderItem initialItem = new OrderItem();
                initialItem.setOrder(order);
                initialItem.setComponent(menuProduct);
                initialItem.setQuantity(quantity != null ? quantity : 1);

                orderItemRepository.saveAndFlush(initialItem);

                // Actualizar precio total
                double totalPrice = menuProduct.getPrice() * initialItem.getQuantity();
                order.setOrderPrice(totalPrice);

                // Usar el método auxiliar para crear JSON simplificado
                order.setOrderBill(createSimplifiedOrderItemsJson(order.getId()));

                orderRepository.save(order);
            } catch (Exception e) {
                throw new RuntimeException("Error al crear OrderItem: " + e.getMessage(), e);
            }
        }

        return order;
    }
    private String createSimplifiedOrderItemsJson(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        List<Map<String, Object>> simplifiedItems = new ArrayList<>();

        for (OrderItem item : items) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("id", item.getId());
            itemMap.put("quantity", item.getQuantity());

            if (item.getComponent() != null) {
                MenuComponent component = item.getComponent();
                Long componentId = component.getId();
                itemMap.put("productId", componentId);

                // Buscar siempre en el repositorio para asegurar datos completos
                MenuProduct product = menuProductRepository.findById(componentId).orElse(null);
                if (product != null) {
                    itemMap.put("productName", product.getName());
                    itemMap.put("productPrice", product.getPrice());
                }
            }

            simplifiedItems.add(itemMap);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(simplifiedItems);
        } catch (Exception e) {
            return "[]";
        }
    }








    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        Order order = orderRepository.findById(orderItem.getOrder().getId())
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        OrderItem savedItem = orderItemRepository.save(orderItem);

        MenuProduct menuProduct = menuProductRepository.findById(orderItem.getComponent().getId())
                .orElseThrow(() -> new IllegalArgumentException("Componente de menú no encontrado"));

        List<OrderItem> allItems = orderItemRepository.findByOrderId(order.getId());

        double totalPrice = 0.0;
        for (OrderItem item : allItems) {
            MenuProduct product = menuProductRepository.findById(item.getComponent().getId())
                    .orElse(null);
            if (product != null) {
                totalPrice += product.getPrice() * item.getQuantity();
            }
        }
        order.setOrderPrice(totalPrice);

        // NUEVO: Usar el método auxiliar para crear JSON simplificado
        order.setOrderBill(createSimplifiedOrderItemsJson(order.getId()));
        orderRepository.save(order);

        return savedItem;
    }


    @Override
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder != null) {
            existingOrder.setOrderBill(order.getOrderBill());
            existingOrder.setOrderPrice(order.getOrderPrice());
            return orderRepository.save(existingOrder);
        }
        return null;
    }

    @Override
    public Order cancelOrder(Long id) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder != null) {
            updateOrderStatus(id, OrderStatusType.CANCELLED);
            return existingOrder;
        }
        return null;
    }

    @Override
    public List<Order> getOrdersByEmployeeId(String employeeId) {
        List<OrderStatus> statuses = orderStatusRepository.findByEmployeeId_Id(employeeId);
        if (statuses == null || statuses.isEmpty()) {
            return List.of();
        }

        List<Long> orderIds = new ArrayList<>();
        for (OrderStatus os : statuses) {
            if (os != null && os.getId() != null && !orderIds.contains(os.getId())) {
                orderIds.add(os.getId());
            }
        }

        if (orderIds.isEmpty()) {
            return List.of();
        }

        return orderRepository.findAllById(orderIds);
    }

    @Override
    public List<OrderItem> getAllOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Override
    public OrderItem updateOrderItemByOrderId(Long orderId, OrderItem orderItem) {
        List<OrderItem> existingOrderItems = orderItemRepository.findByOrderId(orderId);
        if (existingOrderItems != null && !existingOrderItems.isEmpty()) {
            for (OrderItem item : existingOrderItems) {
                if (item.getId().equals(orderItem.getId())) {
                    item.setComponent(orderItem.getComponent());
                    return orderItemRepository.save(item);
                }
            }
        }
        return null;
    }

    @Override
    public void deleteOrderItemByOrderId(Long orderId, Long productId) {

        List<OrderItem> existingOrderItems = orderItemRepository.findByOrderId(orderId);

        if (existingOrderItems != null && !existingOrderItems.isEmpty()) {

            OrderItem targetItem = null;
            for (OrderItem item : existingOrderItems) {
                if (item.getComponent() != null && item.getComponent().getId().equals(productId)) {
                    targetItem = item;
                    break;
                }
            }


            if (targetItem != null) {

                if (targetItem.getQuantity() > 1) {

                    targetItem.setQuantity(targetItem.getQuantity() - 1);
                    orderItemRepository.save(targetItem);
                } else {

                    orderItemRepository.delete(targetItem);
                }

                Order order = orderRepository.findById(orderId).orElse(null);
                if (order != null) {
                    List<OrderItem> remainingItems = orderItemRepository.findByOrderId(orderId);

                    double totalPrice = 0.0;
                    for (OrderItem item : remainingItems) {
                        MenuProduct product = menuProductRepository.findById(item.getComponent().getId())
                                .orElse(null);
                        if (product != null) {
                            totalPrice += product.getPrice() * item.getQuantity();
                        }
                    }
                    order.setOrderPrice(totalPrice);

                    order.setOrderBill(createSimplifiedOrderItemsJson(order.getId()));
                    orderRepository.save(order);
                }
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
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        if (orderIds.isEmpty()) return List.of();
        return orderRepository.findAllById(orderIds);
    }

    @Override
    public OrderStatus updateOrderStatus(Long orderId, OrderStatusType status) {
        if (orderId == null || status == null) return null;

        // Obtiene el estado actual de la orden
        Optional<OrderStatus> lastStatus = orderStatusRepository.findStatusByOrders_Id(orderId);
        OrderStatusType current = lastStatus.isPresent() ? lastStatus.get().getStatus() : null;

        // Si piden CANCELLED, cancelar la orden y guardar el nuevo estado
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
            if (status == OrderStatusType.PENDING) {
                Order order = orderRepository.findById(orderId).orElse(null);
                if (order == null) return null;

                OrderStatus s = new OrderStatus();
                s.setId(orderId);
                s.setOrders(order);
                s.setStatus(status);
                return orderStatusRepository.save(s);
            }
            return null;
        }

        if (status == current) {
            return lastStatus.orElse(null);
        }

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
            return orderStatusRepository.save(newStatus);
        }

        return null;
    }


}
