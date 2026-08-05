package com.ps.productservice.model.dto.feign;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockOperationRequestDTO {
    private Long productId;
    private Integer quantity;
}