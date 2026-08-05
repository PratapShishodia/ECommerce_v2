package com.ps.inventoryservice.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryRequestDTO {
    @NotNull
    private Long productId;
    @Min(0)
    private Integer quantity;
    private String warehouseLocation;
}
