package com.ps.orderservice.model.dto.mapper;

import com.ps.orderservice.model.dto.order.OrderRequestDTO;
import com.ps.orderservice.model.dto.order.OrderResponseDTO;
import com.ps.orderservice.model.entity.Order;

public class OrderDTOMapper {
    public static Order toEntity(OrderRequestDTO requestDTO){
        return Order.builder()
                .userId(requestDTO.getUserId())
                .orderStatus(requestDTO.getOrderStatus())
                .paymentStatus(requestDTO.getPaymentStatus())
                .build();
    }

    public static OrderResponseDTO toDTO(Order order){
        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .orderDate(order.getOrderDate())
                .paymentId(order.getPaymentId())
                .orderItems(order.getOrderItemList().stream().map(OrderItemDTOMapper::toDTO).toList())
                .build();
    }
}
