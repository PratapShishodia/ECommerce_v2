package com.ps.notificationservice.kafka;

import com.ps.common.event.UserEvent;
import com.ps.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "user-topic", groupId = "notification-group")
    public void consume(UserEvent event) {
        try {
            log.info("Received notification for user {}", event.getUserId());
            emailService.sendEmail(event.getRecipient(), event.getSubject(), event.getMessage());
            log.info("Email sent successfully to {}", event.getRecipient());
        } catch (Exception ex) {
            log.error("Failed to process notification for user {}", event.getUserId(), ex);
            throw ex;
        }
    }
}
