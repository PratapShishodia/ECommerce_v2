package com.ps.paymentservice.kafka;

import com.ps.common.event.PaymentNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationStatusProducer {
    private KafkaTemplate<String, PaymentNotificationEvent> kafkaTemplate;

    public void sendUserEvent(PaymentNotificationEvent event){
        try{
            kafkaTemplate.send("payment-notification",event.getUserId().toString() ,event).whenComplete((result,ex) -> {
                if(ex == null){
                    log.info("Payment Notification event published successfully. Offset={}",result.getRecordMetadata().offset());
                }
                else{
                    log.error("Failed to publish Payment Notification event for user {}",event.getUserId(),ex);
                }
            });
        } catch (Exception ex) {
            log.error("Unexpected error while sending Kafka message", ex);
            throw new RuntimeException("Unable to publish Notification event");
        }
    }
}
