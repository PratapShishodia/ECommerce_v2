package com.ps.paymentservice.service;

import com.ps.paymentservice.model.dto.*;
import com.ps.paymentservice.model.dto.common.PageResponseDTO;

public interface PaymentService {
    PaymentResponseDTO initiatePayment(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO verifyPayment(PaymentVerificationRequestDTO verificationRequestDTO);
    PaymentResponseDTO getPaymentById(Long paymentId);
    PaymentResponseDTO getPaymentByOrderId(String orderId);
    PageResponseDTO<PaymentResponseDTO> getPaymentHistory(int page_num,int page_size,Long userId);
    PaymentResponseDTO refundPayment(RefundRequestDTO refundRequestDTO);
    PaymentResponseDTO updatePaymentStatus(PaymentStatusRequestDTO paymentStatusRequestDTO);
}
