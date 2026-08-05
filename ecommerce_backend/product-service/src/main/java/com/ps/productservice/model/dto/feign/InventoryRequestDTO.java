package com.ps.productservice.model.dto.feign;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryRequestDTO {
    private Long productId;
    private Integer quantity;
    private String warehouseLocation;
}
