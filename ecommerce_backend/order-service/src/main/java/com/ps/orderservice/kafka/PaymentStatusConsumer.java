package com.ps.orderservice.kafka;

import com.ps.common.event.PaymentStatusEvent;
import com.ps.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentStatusConsumer {
    private final OrderService orderService;

    @KafkaListener(topics = "payment-status",groupId = "payment-group")
    public void consume(PaymentStatusEvent event) {
        try {
            log.info("Received Payment Status Event for order {}", event.getOrderId());
            orderService.updatePaymentStatus(event.getOrderId(), event.getPaymentStatus());
            orderService.updatePaymentId(event.getOrderId(), event.getPaymentId());
            log.info("Payment Status updated successfully for order {}", event.getOrderId());
        } catch (Exception ex) {
            log.error("Failed to process payment Status event for order {}", event.getOrderId(), ex);
            throw ex;
        }
    }
}
