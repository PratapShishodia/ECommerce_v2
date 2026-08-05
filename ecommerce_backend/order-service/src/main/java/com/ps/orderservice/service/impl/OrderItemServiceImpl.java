package com.ps.orderservice.service.impl;

import com.ps.common.event.InventoryStockUpdatesEvent;
import com.ps.orderservice.kafka.InventoryEventProducer;
import com.ps.orderservice.model.dto.mapper.OrderItemDTOMapper;
import com.ps.orderservice.model.dto.orderItem.OrderItemRequestDTO;
import com.ps.orderservice.model.entity.OrderItem;
import com.ps.orderservice.repository.OrderItemRepo;
import com.ps.orderservice.service.OrderItemService;
import com.ps.orderservice.webClient.InventoryClient;
import com.ps.orderservice.webClient.ProductClient;
import com.ps.orderservice.webClient.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepo orderItemRepo;
    private final InventoryClient inventoryClient;
    private final ProductClient productClient;
    private final InventoryEventProducer inventoryEventProducer;

    @Override
    public OrderItem createOrderItem(String orderId, OrderItemRequestDTO orderItemRequestDTO) {
//        Add WebClient
        ProductDTO productDTO = productClient.getProductById(orderItemRequestDTO.getProductId());
//        Check Availability
        if(!inventoryClient.checkAvailability(productDTO.getProductId())) {
            throw new RuntimeException("Out of Stock");
        }

        OrderItem orderItem = OrderItemDTOMapper.toEntity(orderItemRequestDTO);
        orderItem.setPrice(productDTO.getProductPrice());
        orderItem.setSubTotal(productDTO.getProductPrice().multiply(BigDecimal.valueOf(orderItemRequestDTO.getQuantity())));

//        Reserve Stock
        InventoryStockUpdatesEvent updatesEvent = InventoryStockUpdatesEvent.builder()
                .orderId(orderId)
                .quantity(orderItemRequestDTO.getQuantity())
                .productId(orderItem.getProductId())
                .action("RESERVE")
                .build();
        inventoryEventProducer.sendInventoryEvent(updatesEvent);
//        inventoryClient.reserveStock(StockOperationRequestDTO.builder().productId(orderItem.getProductId()).quantity(orderItemRequestDTO.getQuantity()).build());
        orderItemRepo.save(orderItem);
        return orderItem;
    }
}
