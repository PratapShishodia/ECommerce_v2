package com.ps.paymentservice.service.impl;

import com.ps.common.event.PaymentNotificationEvent;
import com.ps.common.event.PaymentStatusEvent;
import com.ps.paymentservice.customExceptions.ResourceNotFoundException;
import com.ps.paymentservice.kafka.NotificationStatusProducer;
import com.ps.paymentservice.kafka.PaymentStatusEventProducer;
import com.ps.paymentservice.model.dto.*;
import com.ps.paymentservice.model.dto.common.PageResponseDTO;
import com.ps.paymentservice.model.dto.feign.NotificationRequestDTO;
import com.ps.paymentservice.model.entity.Payment;
import com.ps.paymentservice.model.mapper.PaymentDTOMapper;
import com.ps.paymentservice.repository.PaymentRepo;
import com.ps.paymentservice.service.PaymentService;
import com.ps.paymentservice.util.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepo paymentRepo;
    private final PaymentStatusEventProducer paymentStatusEventProducer;
    private final NotificationStatusProducer notificationStatusProducer;
//    private final OrderClient orderClient;
//    private final NotificationClient notificationClient;

    @Override
    @Transactional
    public PaymentResponseDTO initiatePayment(PaymentRequestDTO paymentRequestDTO) {
        UserDTO user = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Payment  payment = PaymentDTOMapper.toEntity(paymentRequestDTO);
        payment.setUserId(Long.valueOf(user.getUserId()));
        payment.setTransactionId(UUID.randomUUID());
        payment.setStatus("PENDING");
        payment.setPaymentDate(LocalDateTime.now());
        Payment saved = paymentRepo.save(payment);
        //Update Status in Order
        paymentStatusEventProducer.sendPaymentStatusEvent(PaymentStatusEvent.builder().paymentStatus("PENDING").paymentId(saved.getPaymentId()).orderId(paymentRequestDTO.getOrderId()).build());
//        orderClient.updatePaymentStatus(paymentRequestDTO.getOrderId(), "PENDING");
//        orderClient.updatePaymentID(paymentRequestDTO.getOrderId(),saved.getPaymentId());
        return PaymentDTOMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public PaymentResponseDTO verifyPayment(PaymentVerificationRequestDTO verificationRequestDTO) {
        UserDTO user = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Payment payment = paymentRepo.findById(verificationRequestDTO.getPaymentId()).orElse(null);
        String message = "";
        String subject = "";
        //Update Status in Order
        if(payment==null){
            paymentStatusEventProducer.sendPaymentStatusEvent(PaymentStatusEvent.builder().paymentStatus("FAILED").paymentId(verificationRequestDTO.getPaymentId()).orderId(verificationRequestDTO.getOrderId()).build());
//            orderClient.updatePaymentStatus(verificationRequestDTO.getOrderId(), "FAILED");
            subject = "Payment Failed - #ORD"+verificationRequestDTO.getOrderId();
            message = "Dear "+user.getUsername()+",\n\nWe were unable to process your payment for your recent order.\n\nPayment Details:\n- Order ID: ORD202607210001\n- Transaction ID: TXN8F4A92C1D7\n- Payment Amount: ₹"+ NumberFormat.getNumberInstance(new Locale("en", "IN")).format(payment.getAmount())+"\n- Payment Status: FAILED\n- Failure Time: "+payment.getPaymentDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a"))+" IST\n- Reason: Insufficient Balance\n\nNo amount has been charged to your account. Please try again using the same or a different payment method.\n\nIf the amount was deducted, it will be automatically refunded by your bank as per their processing timeline.\n\nRegards,\nE-Commerce Team";
        }
        else {
            payment.setStatus("SUCCESS");
            subject = "Order Confirmed - #ORD"+verificationRequestDTO.getOrderId();
            message = "Dear "+user.getUsername()+",\n\nThank you for shopping with us!\n\nYour order has been successfully placed and your payment has been received.\n\nOrder Details:\n- Order ID: ORD"+verificationRequestDTO.getOrderId()+"\n- Transaction ID: TXN"+payment.getTransactionId()+"\n- Payment Amount: ₹"+ NumberFormat.getNumberInstance(new Locale("en", "IN")).format(payment.getAmount())+"\n- Payment Status: SUCCESS\n- Order Date & Time: "+payment.getPaymentDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a"))+" IST\n\nYour order is now being processed. You will receive another notification once it has been shipped.\n\nThank you for choosing our store!\n\nRegards,\nE-Commerce Team";
            paymentStatusEventProducer.sendPaymentStatusEvent(PaymentStatusEvent.builder().paymentStatus("SUCCESS").paymentId(verificationRequestDTO.getPaymentId()).orderId(verificationRequestDTO.getOrderId()).build());
//            orderClient.updatePaymentStatus(verificationRequestDTO.getOrderId(), "SUCCESS");
        }
//                .orElseThrow(()-> new ResourceNotFoundException("Payment Details","Payment ID",String.valueOf(verificationRequestDTO.getPaymentId())));

        //Send Notification
//        NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO();
//        notificationRequestDTO.setUserId(Long.valueOf(user.getUserId()));
//        notificationRequestDTO.setRecipient(user.getEmail());
//        notificationRequestDTO.setSubject(subject);
//        notificationRequestDTO.setMessage(message);
//        notificationClient.sendEmail(notificationRequestDTO);
        notificationStatusProducer.sendUserEvent(PaymentNotificationEvent.builder()
                        .message(message)
                        .subject(subject)
                        .userId(Long.valueOf(user.getUserId()))
                        .recipient(user.getEmail())
                .build());
        return PaymentDTOMapper.toDTO(paymentRepo.save(payment));
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long paymentId) {
        return PaymentDTOMapper.toDTO(paymentRepo.findById(paymentId).orElseThrow(()-> new ResourceNotFoundException("Payment Details","Payment ID",String.valueOf(paymentId))));
    }

    @Override
    public PaymentResponseDTO getPaymentByOrderId(String orderId) {
        return PaymentDTOMapper.toDTO(paymentRepo.findByOrderId(orderId).orElseThrow(()-> new ResourceNotFoundException("Payment Details","Payment ID",String.valueOf(orderId))));
    }

    @Override
    public PageResponseDTO<PaymentResponseDTO> getPaymentHistory(int page_num,int page_size,Long userId) {
        Pageable pageable = PageRequest.of(page_num, page_size);
        Page<Payment> paymentPage = paymentRepo.findByUserId(userId,pageable);
        List<PaymentResponseDTO> paymentList = paymentPage.getContent().stream().map(PaymentDTOMapper::toDTO).toList();
        PageResponseDTO<PaymentResponseDTO> response = new PageResponseDTO<>();
        response.setContent(paymentList);
        response.setPageNumber(paymentPage.getNumber());
        response.setPageSize(paymentPage.getSize());
        response.setTotalElements(paymentPage.getTotalElements());
        response.setTotalPages(paymentPage.getTotalPages());
        response.setLastPage(paymentPage.isLast());
        return response;
    }

    @Override
    @Transactional
    public PaymentResponseDTO refundPayment(RefundRequestDTO refundRequestDTO) {
        UserDTO user = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Payment payment = paymentRepo.findById(refundRequestDTO.getPaymentId()).orElseThrow(()->new ResourceNotFoundException("Payment Details","Payment ID",String.valueOf(refundRequestDTO.getPaymentId())));
        payment.setStatus("REFUNDED");
//        Send Notification
//        NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO();
//        notificationRequestDTO.setUserId(Long.valueOf(user.getUserId()));
//        notificationRequestDTO.setRecipient(user.getEmail());
//        notificationRequestDTO.setSubject("Refund Initiated - Order #TRN"+payment.getTransactionId());
//        notificationRequestDTO.setMessage("Dear "+user.getUsername()+",\n\nYour refund has been successfully processed.\n\nRefund Details:\n- Order ID: ORD"+refundRequestDTO.getOrderId()+"\n- Original Transaction ID: TXN"+ payment.getTransactionId()+"\n- Refund Amount: ₹"+ NumberFormat.getNumberInstance(new Locale("en", "IN")).format(payment.getAmount())+"\n- Refund Status: SUCCESS\n- Refund Date & Time: "+payment.getPaymentDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a"))+" IST\n\nThe refunded amount has been credited to your original payment method. If you do not see the credit immediately, please check with your bank.\n\nThank you for shopping with us.\n\nRegards,\nE-Commerce Team");
//        notificationClient.sendEmail(notificationRequestDTO);
        notificationStatusProducer.sendUserEvent(PaymentNotificationEvent.builder()
                .message("Dear "+user.getUsername()+",\n\nYour refund has been successfully processed.\n\nRefund Details:\n- Order ID: ORD"+refundRequestDTO.getOrderId()+"\n- Original Transaction ID: TXN"+ payment.getTransactionId()+"\n- Refund Amount: ₹"+ NumberFormat.getNumberInstance(new Locale("en", "IN")).format(payment.getAmount())+"\n- Refund Status: SUCCESS\n- Refund Date & Time: "+payment.getPaymentDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a"))+" IST\n\nThe refunded amount has been credited to your original payment method. If you do not see the credit immediately, please check with your bank.\n\nThank you for shopping with us.\n\nRegards,\nE-Commerce Team")
                .subject("Refund Initiated - Order #TRN"+payment.getTransactionId())
                .userId(Long.valueOf(user.getUserId()))
                .recipient(user.getEmail())
                .build());
        return PaymentDTOMapper.toDTO(paymentRepo.save(payment));
    }

    @Override
    @Transactional
    public PaymentResponseDTO updatePaymentStatus(PaymentStatusRequestDTO paymentStatusRequestDTO) {
        Payment payment = paymentRepo.findById(paymentStatusRequestDTO.getPaymentId()).orElseThrow(()->new ResourceNotFoundException("Payment Details","Payment ID",String.valueOf(paymentStatusRequestDTO.getPaymentId())));
        payment.setStatus(paymentStatusRequestDTO.getStatus());
        return PaymentDTOMapper.toDTO(paymentRepo.save(payment));
    }
}
