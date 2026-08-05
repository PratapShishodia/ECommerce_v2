package com.ps.inventoryservice.kafka;

import com.ps.common.event.InventoryStockUpdatesEvent;
import com.ps.inventoryservice.model.dto.StockOperationRequestDTO;
import com.ps.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryUpdateConsumer {
    private final InventoryService inventoryService;

    @KafkaListener(topics = "inventory-stock-update",groupId = "inventory-group")
    public void consume(InventoryStockUpdatesEvent event){
        try {
            log.info("Received Inventory update Event for order {}", event.getOrderId());
            StockOperationRequestDTO operationRequestDTO = StockOperationRequestDTO.builder()
                    .productId(event.getProductId())
                    .quantity(event.getQuantity())
                    .build();
            switch (event.getAction()) {
                case "DEDUCT" -> inventoryService.deductStock(operationRequestDTO);
                case "RELEASE" -> inventoryService.releaseStock(operationRequestDTO);
                case "RESERVE" -> inventoryService.reserveStock(operationRequestDTO);
            }
            log.info("Processed Inventory update Event for order {}", event.getOrderId());
        } catch (Exception ex) {
            log.error("Failed to process notification for user {}", event.getOrderId(), ex);
            throw ex;
        }
    }
}
