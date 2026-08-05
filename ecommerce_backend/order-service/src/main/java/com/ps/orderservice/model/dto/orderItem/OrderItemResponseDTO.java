package com.ps.orderservice.model.dto.orderItem;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponseDTO {
    private Long orderItemId;
    private Long productId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}