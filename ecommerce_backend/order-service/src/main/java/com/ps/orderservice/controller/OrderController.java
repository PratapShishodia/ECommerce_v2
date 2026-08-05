package com.ps.orderservice.controller;

import com.ps.orderservice.model.dto.common.PageResponseDTO;
import com.ps.orderservice.model.dto.order.OrderRequestDTO;
import com.ps.orderservice.model.dto.order.OrderResponseDTO;
import com.ps.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO requestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(requestDTO));
    }

    @PutMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@RequestParam String orderId){
        return ResponseEntity.accepted().body(orderService.cancelOrder(orderId));
    }

    @PutMapping("/update")
    public ResponseEntity<OrderResponseDTO> updateOrder(@RequestParam String orderId,@RequestBody OrderRequestDTO requestDTO){
        return ResponseEntity.accepted().body(orderService.updateOrder(orderId,requestDTO));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getByOrderId(@PathVariable String orderId){
        return ResponseEntity.accepted().body(orderService.getByOrderId(orderId));
    }

    @GetMapping("/history")
    public ResponseEntity<PageResponseDTO> getAll(@RequestParam int page_num,@RequestParam int page_size,@RequestParam long userId){
        return ResponseEntity.accepted().body(orderService.getAllOrders(page_num-1,page_size-1,userId));
    }

    @PutMapping("/updatePaymentStatus")
    public ResponseEntity<String> updatePaymentStatus(@RequestParam String orderId,@RequestParam String status){
        return ResponseEntity.accepted().body(orderService.updatePaymentStatus(orderId,status));
    }
    @PutMapping("/updatePaymentId")
    public ResponseEntity<String> updatePaymentId(@RequestParam String orderId,@RequestParam Long paymentId){
        return ResponseEntity.accepted().body(orderService.updatePaymentId(orderId,paymentId));
    }
}
