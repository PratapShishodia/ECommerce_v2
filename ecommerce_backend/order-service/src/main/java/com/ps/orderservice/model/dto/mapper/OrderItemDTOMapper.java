package com.ps.orderservice.model.dto.mapper;

import com.ps.orderservice.model.dto.order.OrderRequestDTO;
import com.ps.orderservice.model.dto.orderItem.OrderItemRequestDTO;
import com.ps.orderservice.model.dto.orderItem.OrderItemResponseDTO;
import com.ps.orderservice.model.entity.Order;
import com.ps.orderservice.model.entity.OrderItem;

public class OrderItemDTOMapper {
    public static OrderItemResponseDTO toDTO(OrderItem orderItem){
        return OrderItemResponseDTO.builder()
                .orderItemId(orderItem.getOrderItemId())
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .subTotal(orderItem.getSubTotal())
                .build();
    }

    public static OrderItem toEntity(OrderItemRequestDTO requestDTO){
        return OrderItem.builder()
                .productId(requestDTO.getProductId())
                .quantity(requestDTO.getQuantity())
                .build();
    }
}
