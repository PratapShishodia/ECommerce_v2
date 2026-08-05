package com.ps.orderservice.model.dto.order;

import com.ps.orderservice.model.dto.orderItem.OrderItemRequestDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDTO {
    private Long userId;
    private String orderStatus;
    private String paymentStatus;
    private List<OrderItemRequestDTO> orderItemRequestDTOList;
}
