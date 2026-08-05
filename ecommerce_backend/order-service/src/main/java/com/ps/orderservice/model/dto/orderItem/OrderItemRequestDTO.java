package com.ps.orderservice.model.dto.orderItem;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequestDTO {
    private Long productId;
    private int quantity;
}