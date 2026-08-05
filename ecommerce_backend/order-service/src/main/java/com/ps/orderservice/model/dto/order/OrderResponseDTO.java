package com.ps.orderservice.model.dto.order;

import com.ps.orderservice.model.dto.orderItem.OrderItemResponseDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private String orderId;
    private Long userId;
    private BigDecimal amount;
    private String orderStatus;
    private String paymentStatus;
    private LocalDateTime orderDate;
    private Long paymentId;
    List<OrderItemResponseDTO> orderItems;
}
