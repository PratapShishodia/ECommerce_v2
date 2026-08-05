package com.ps.inventoryservice.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockOperationRequestDTO {
    @NotNull
    private Long productId;
    @Min(1)
    private Integer quantity;
}
