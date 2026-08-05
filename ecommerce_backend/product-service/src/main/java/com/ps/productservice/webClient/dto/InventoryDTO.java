package com.ps.productservice.webClient.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryDTO {
    private Long productId;
    private Integer quantity;
    private String warehouseLocation;
}
