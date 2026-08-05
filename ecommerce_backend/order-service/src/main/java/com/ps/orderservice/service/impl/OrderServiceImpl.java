package com.ps.orderservice.service.impl;

import com.ps.common.event.InventoryStockUpdatesEvent;
import com.ps.common.event.PaymentRefundEvent;
import com.ps.orderservice.customExceptions.ResourceNotFoundException;
import com.ps.orderservice.kafka.InventoryEventProducer;
import com.ps.orderservice.model.dto.common.PageResponseDTO;
import com.ps.orderservice.model.dto.common.UserDTO;
import com.ps.orderservice.model.dto.mapper.OrderDTOMapper;
import com.ps.orderservice.model.dto.order.OrderRequestDTO;
import com.ps.orderservice.model.dto.order.OrderResponseDTO;
import com.ps.orderservice.model.entity.Order;
import com.ps.orderservice.model.entity.OrderItem;
import com.ps.orderservice.repository.OrderRepo;
import com.ps.orderservice.service.OrderItemService;
import com.ps.orderservice.service.OrderService;
import com.ps.orderservice.webClient.InventoryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderItemService orderItemService;
    private OrderRepo orderRepo;
    private InventoryEventProducer inventoryEventProducer;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        String orderID = UUID.randomUUID().toString().split("-")[0];
        UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = OrderDTOMapper.toEntity(request);
        order.setOrderId(orderID);
        List<OrderItem> orderItems = request.getOrderItemRequestDTOList()
                .stream()
                .map(item -> orderItemService.createOrderItem(orderID,item))
                .toList();

        orderItems.forEach(item -> item.setOrder(order));

        order.setOrderItemList(orderItems);
        order.setUserId(Long.valueOf(userDTO.getUserId()));
        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());
        return OrderDTOMapper.toDTO(orderRepo.save(order));
    }

    @Override
    public String cancelOrder(String orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","Order Id",String.valueOf(orderId)));
        if(order.getPaymentStatus().equals("PENDING") || order.getPaymentStatus().equals("FAILED")) {
            Map<Long,Integer> itemList = order.getOrderItemList().stream().collect(Collectors.toMap(OrderItem::getProductId,OrderItem::getQuantity));
            for(Long itemId : itemList.keySet()){
//                Add webClient
                InventoryStockUpdatesEvent updatesEvent = InventoryStockUpdatesEvent.builder()
                        .orderId(orderId)
                        .quantity(itemList.get(itemId))
                        .productId(itemId)
                        .action("RESERVE")
                        .build();
                inventoryEventProducer.sendInventoryEvent(updatesEvent);
//                inventoryClient.releaseStock(StockOperationRequestDTO.builder().productId(itemId).quantity(itemList.get(itemId)).build());
            }
        }
        if(order.getPaymentStatus().equals("SUCCESS")){
//                Add kafka Producer
            PaymentRefundEvent paymentRefundEvent = PaymentRefundEvent.builder()
                    .paymentId(order.getPaymentId())
                    .orderId(orderId)
                    .refundAmount(order.getAmount())
                    .reason("ORDER CANCELLED")
                    .build();
//            paymentClient.refundPayment(RefundRequestDTO.builder().paymentId(order.getPaymentId()).orderId(orderId).refundAmount(order.getAmount()).build());
        }
        order.setOrderStatus("CANCELLED");
        return "Order Cancelled";
    }

    @Override
    public OrderResponseDTO updateOrder(String orderId, OrderRequestDTO requestDTO) {
        return null;
    }

    @Override
    public OrderResponseDTO getByOrderId(String orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","Order Id",String.valueOf(orderId)));
        return OrderDTOMapper.toDTO(order);
    }

    @Override
    public PageResponseDTO<OrderResponseDTO> getAllOrders(int page_num, int page_size, Long userId) {
        Pageable pageable = PageRequest.of(page_num, page_size);
        Page<Order> productPage = orderRepo.findByUserId(userId,pageable);
        List<OrderResponseDTO> productList = productPage.getContent().stream().map(OrderDTOMapper::toDTO).toList();
        PageResponseDTO<OrderResponseDTO> response = new PageResponseDTO<>();
        response.setContent(productList);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalElements(productPage.getTotalElements());
        response.setTotalPages(productPage.getTotalPages());
        response.setLastPage(productPage.isLast());
        return response;
    }

    @Override
    public String updatePaymentStatus(String orderId, String status) {
        Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","Order Id",String.valueOf(orderId)));
        order.setPaymentStatus(status);
        if(status.equals("SUCCESS")){
            Map<Long,Integer> itemList = order.getOrderItemList().stream().collect(Collectors.toMap(OrderItem::getProductId,OrderItem::getQuantity));
            for(Long itemId : itemList.keySet()){
//                Add WebClient
                InventoryStockUpdatesEvent updatesEvent = InventoryStockUpdatesEvent.builder()
                        .orderId(orderId)
                        .quantity(itemList.get(itemId))
                        .productId(itemId)
                        .action("DEDUCT")
                        .build();
                inventoryEventProducer.sendInventoryEvent(updatesEvent);
//                inventoryClient.deductStock(StockOperationRequestDTO.builder().productId(itemId).quantity(itemList.get(itemId)).build());
            }
        }
        if(status.equals("FAILED")){
            Map<Long,Integer> itemList = order.getOrderItemList().stream().collect(Collectors.toMap(OrderItem::getProductId,OrderItem::getQuantity));
            for(Long itemId : itemList.keySet()){
//                Add WebClient
                InventoryStockUpdatesEvent updatesEvent = InventoryStockUpdatesEvent.builder()
                        .orderId(orderId)
                        .quantity(itemList.get(itemId))
                        .productId(itemId)
                        .action("RELEASE")
                        .build();
                inventoryEventProducer.sendInventoryEvent(updatesEvent);
//                inventoryClient.releaseStock(StockOperationRequestDTO.builder().productId(itemId).quantity(itemList.get(itemId)).build());
            }
        }
        return "Updated Payment Status";
    }

    @Override
    public String updatePaymentId(String orderId, Long paymentId) {
        Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","Order Id",String.valueOf(orderId)));
        order.setPaymentId(paymentId);
        return "Updated Payment ID";
    }
}
