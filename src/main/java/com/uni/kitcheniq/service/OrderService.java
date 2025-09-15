package com.uni.kitcheniq.service;
import com.uni.kitcheniq.enums.OrderStatusType;
import com.uni.kitcheniq.models.OrderItem;
import com.uni.kitcheniq.models.OrderStatus;
import com.uni.kitcheniq.repository.OrderItemRepository;
import com.uni.kitcheniq.repository.OrderRepository;
import com.uni.kitcheniq.repository.OrderStatusRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.uni.kitcheniq.models.Order;
import org.springframework.beans.factory.annotation.Autowired;

@Service

public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;

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
        return orderRepository.save(order);
        //debo hacer creación en cascada de orderItems y orderStatus
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder != null) {
            existingOrder.setOrderBill(order.getOrderBill());
            existingOrder.setOrderDetails(order.getOrderDetails());
            existingOrder.setOrderPrice(order.getOrderPrice());
            return orderRepository.save(existingOrder);
        }
        return null;
    }

    @Override
    public Order cancelOrder(Long id) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder != null) {
            //existingOrder.setStatus("Cancelled");
            return orderRepository.save(existingOrder);
        }
        return null;
    }

    @Override
    public List<Order> getOrdersByEmployeeId(String employeeId) {
        List<OrderStatus> statuses = orderStatusRepository.findByEmployeeId_Id(employeeId);
        if (statuses == null || statuses.isEmpty()) {
            return List.of();
        }

        java.util.List<Long> orderIds = new java.util.ArrayList<>();
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
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem updateOrderItemByOrderId(Long OrderId, OrderItem orderItem) {
        List<OrderItem> existingOrder = orderItemRepository.findByOrderId(OrderId);
        if (existingOrder != null) {
            for (OrderItem item : existingOrder) {
                item.setComponent( orderItem.getComponent());
                item.setQuantity(orderItem.getQuantity());
                orderItemRepository.save(item);
            }
        }
        return null;
    }

    @Override
    public void deleteOrderItemByOrderId(Long OrderId, Long orderItemId) {
        List<OrderItem> existingOrder = orderItemRepository.findByOrderId(OrderId);
        if (existingOrder != null) {
            orderItemRepository.deleteById(orderItemId);

        }

    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatusType statusType) {
        if (statusType == null) return java.util.List.of();

        List<OrderStatus> statuses = orderStatusRepository.findAll();
        if (statuses == null || statuses.isEmpty()) return java.util.List.of();

        java.util.List<Long> orderIds = statuses.stream()
                .filter(java.util.Objects::nonNull)
                .filter(s -> statusType.equals(s.getStatus()))
                .map(OrderStatus::getId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .collect(java.util.stream.Collectors.toList());

        if (orderIds.isEmpty()) return java.util.List.of();
        return orderRepository.findAllById(orderIds);
    }

    @Override
    public OrderStatus updateOrderStatus(Long orderId, OrderStatusType status) {
        if (orderId == null || status == null) return null;

        // Obtiene el estado actual (único) de la orden
        Optional<OrderStatus> lastStatus = orderStatusRepository.findStatusByOrders_Id(orderId);
        OrderStatusType current = lastStatus.isPresent() ? lastStatus.get().getStatus() : null;

        // Si piden CANCELLED, cancelar la orden y guardar el nuevo estado
        if (status == OrderStatusType.CANCELLED) {
            cancelOrder(orderId);
            OrderStatus cancelled = new OrderStatus();
            cancelled.setId(orderId);
            cancelled.setStatus(status);
            return orderStatusRepository.save(cancelled);
        }

        // Si no hay estado previo, solo permitir crear PENDING
        if (current == null) {
            if (status == OrderStatusType.PENDING) {
                OrderStatus s = new OrderStatus();
                s.setId(orderId);
                s.setStatus(status);
                return orderStatusRepository.save(s);
            }
            return null;
        }

        // Si piden el mismo estado, devolver el último registrado
        if (status == current) {
            return lastStatus.orElse(null);
        }

        // Mapa de transiciones válidas
        java.util.Map<OrderStatusType, OrderStatusType> validNext = java.util.Map.of(
                OrderStatusType.PENDING, OrderStatusType.IN_PROGRESS,
                OrderStatusType.IN_PROGRESS, OrderStatusType.COMPLETED,
                OrderStatusType.COMPLETED, OrderStatusType.SERVED
        );

        OrderStatusType expectedNext = validNext.get(current);
        if (expectedNext != null && expectedNext == status) {
            OrderStatus newStatus = new OrderStatus();
            newStatus.setId(orderId);
            newStatus.setStatus(status);
            return orderStatusRepository.save(newStatus);
        }

        // Transición no permitida
        return null;
    }


}
