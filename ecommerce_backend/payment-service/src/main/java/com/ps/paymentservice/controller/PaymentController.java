package com.ps.paymentservice.controller;

import com.ps.paymentservice.model.dto.*;
import com.ps.paymentservice.model.dto.common.PageResponseDTO;
import com.ps.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@RequestBody PaymentRequestDTO paymentRequestDTO){
        return ResponseEntity.ok(paymentService.initiatePayment(paymentRequestDTO));
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponseDTO> verifyPayment(@RequestBody PaymentVerificationRequestDTO requestDTO){
        return ResponseEntity.ok(paymentService.verifyPayment(requestDTO));
    }

    @PostMapping("/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment(@RequestBody RefundRequestDTO requestDTO){
        return ResponseEntity.ok(paymentService.refundPayment(requestDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<PaymentResponseDTO> updatePayment(@RequestBody PaymentStatusRequestDTO paymentStatusRequestDTO){
        return ResponseEntity.ok(paymentService.updatePaymentStatus(paymentStatusRequestDTO));
    }

    @GetMapping("/id")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@RequestParam Long paymentId){
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/order")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(@RequestParam String orderId){
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @GetMapping("/history")
    public ResponseEntity<PageResponseDTO<PaymentResponseDTO>> getPaymentHistory(@RequestParam int page_num, @RequestParam int page_size, @RequestParam Long userId){
        return ResponseEntity.ok(paymentService.getPaymentHistory(page_num,page_size,userId));
    }
}
