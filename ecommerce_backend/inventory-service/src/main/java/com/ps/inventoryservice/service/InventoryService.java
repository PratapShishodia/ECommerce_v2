package com.ps.inventoryservice.service;

import com.ps.inventoryservice.model.dto.BulkInventoryRequestDTO;
import com.ps.inventoryservice.model.dto.InventoryRequestDTO;
import com.ps.inventoryservice.model.dto.InventoryResponseDTO;
import com.ps.inventoryservice.model.dto.StockOperationRequestDTO;

import java.util.List;

public interface InventoryService {
    InventoryResponseDTO createInventory(InventoryRequestDTO requestDTO);
    InventoryResponseDTO getInventoryByProductId(Long productId);
    InventoryResponseDTO updateStock(Long productId,InventoryRequestDTO requestDTO);
    InventoryResponseDTO reserveStock(StockOperationRequestDTO requestDTO);
    InventoryResponseDTO releaseStock(StockOperationRequestDTO requestDTO);
    InventoryResponseDTO deductStock(StockOperationRequestDTO requestDTO);
    boolean checkAvailability(Long productId);
    List<InventoryResponseDTO> bulkUpdate(BulkInventoryRequestDTO requestDTO);
}
