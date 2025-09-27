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
    Order updateOrder(Long id, Order order);
    Order cancelOrder(Long id);
    List<Order> getOrdersByEmployeeId(String employeeId);
    List<OrderItem> getAllOrderItemsByOrderId(Long orderId);
    OrderItem createOrderItem(OrderItem orderItem);
    OrderItem updateOrderItemByOrderId(Long OrderId, OrderItem orderItem);
    void deleteOrderItemByOrderId(Long OrderId, Long orderItemId);
    List<Order> getOrdersByStatus(OrderStatusType status);
    OrderStatus updateOrderStatus(Long orderId, OrderStatusType status);
    Order createOrderWithInitialItem(String employeeId, Long menuComponentId, Integer quantity);
}
