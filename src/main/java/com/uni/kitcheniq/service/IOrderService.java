package com.uni.kitcheniq.service;

import com.uni.kitcheniq.enums.OrderStatusType;
import com.uni.kitcheniq.models.Order;
import com.uni.kitcheniq.models.OrderItem;
import com.uni.kitcheniq.models.OrderStatus;

import java.util.List;

public interface IOrderService {
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order createOrder(Order order);
    Order createOrderWithInitialItem(String employeeId, Long menuComponentId, Integer quantity, Integer tableNumber);
    Order updateOrder(Long id, Order order);
    Order cancelOrder(Long id);
    List<Order> getOrdersByEmployeeId(String employeeId);
    List<OrderItem> getAllOrderItemsByOrderId(Long orderId);
    OrderItem createOrderItem(OrderItem orderItem);
    OrderItem updateOrderItemByOrderId(Long orderId, OrderItem orderItem);
    void deleteOrderItemByOrderId(Long orderId, Long productId);
    List<Order> getOrdersByStatus(com.uni.kitcheniq.enums.OrderStatusType statusType);
    OrderStatus updateOrderStatus(Long orderId, OrderStatusType status);
}
