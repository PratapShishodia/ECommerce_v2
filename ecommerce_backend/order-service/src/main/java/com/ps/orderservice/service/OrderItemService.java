package com.ps.orderservice.service;

import com.ps.orderservice.model.dto.orderItem.OrderItemRequestDTO;
import com.ps.orderservice.model.entity.OrderItem;

public interface OrderItemService {
    OrderItem createOrderItem(String orderId, OrderItemRequestDTO orderItemRequestDTO);
}
