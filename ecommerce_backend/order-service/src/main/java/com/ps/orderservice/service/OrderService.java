package com.ps.orderservice.service;

import com.ps.orderservice.model.dto.common.PageResponseDTO;
import com.ps.orderservice.model.dto.order.OrderRequestDTO;
import com.ps.orderservice.model.dto.order.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);
    String cancelOrder(String orderId);
    OrderResponseDTO updateOrder(String orderId,OrderRequestDTO requestDTO);
    OrderResponseDTO getByOrderId(String orderId);
    PageResponseDTO<OrderResponseDTO> getAllOrders(int page_num,int page_size,Long userId);
    String updatePaymentStatus(String orderId, String status);
    String updatePaymentId(String orderId, Long paymentId);
}
