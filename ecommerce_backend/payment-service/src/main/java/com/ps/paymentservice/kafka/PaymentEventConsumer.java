package com.ps.paymentservice.kafka;

import com.ps.common.event.PaymentRefundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.ps.paymentservice.service.PaymentService;
import com.ps.paymentservice.model.dto.RefundRequestDTO;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventConsumer {
    private final PaymentService paymentService;

    @KafkaListener(topics = "inventory-stock-update",groupId = "inventory-group")
    public void consume(PaymentRefundEvent refundEvent){
        try{
            log.info("Received payment for Order {}", refundEvent.getOrderId());
            RefundRequestDTO refundRequestDTO = RefundRequestDTO.builder()
                    .paymentId(refundEvent.getPaymentId())
                    .refundAmount(refundEvent.getRefundAmount())
                    .reason(refundEvent.getReason())
                    .orderId(refundEvent.getOrderId())
                    .build();
            paymentService.refundPayment(refundRequestDTO);
            log.info("Refund Processed Successfully for Order {}",refundEvent.getOrderId());
        } catch (Exception ex) {
            log.error("Failed to process Refund for Order {}", refundEvent.getOrderId(), ex);
            throw ex;
        }
    }
 }
