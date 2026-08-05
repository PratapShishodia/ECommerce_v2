package com.ps.notificationservice.kafka;

import com.ps.common.event.PaymentNotificationEvent;
import com.ps.common.event.UserCreatedEvent;
import com.ps.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentNotificationConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic", groupId = "notification-group")
    public void consume(PaymentNotificationEvent event) {
        try {
            log.info("Received payment notification for user {}", event.getUserId());
            emailService.sendEmail(event.getRecipient(), event.getSubject(), event.getMessage());
            log.info("Payment Email sent successfully to {}", event.getRecipient());
        } catch (Exception ex) {
            log.error("Failed to process payment notification for user {}", event.getUserId(), ex);
            throw ex;
        }
    }
}
