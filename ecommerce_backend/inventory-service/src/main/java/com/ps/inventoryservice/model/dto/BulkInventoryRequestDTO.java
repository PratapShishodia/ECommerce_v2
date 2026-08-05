package com.ps.inventoryservice.model.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkInventoryRequestDTO {
    private List<InventoryRequestDTO> inventories;
}
