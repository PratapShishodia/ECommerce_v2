package com.ps.paymentservice.kafka;

import com.ps.common.event.PaymentStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusEventProducer {
    private KafkaTemplate<String, PaymentStatusEvent> kafkaTemplate;

    public void sendPaymentStatusEvent(PaymentStatusEvent event){
        try{
            kafkaTemplate.send("payment-status",event.getOrderId() ,event).whenComplete((result,ex) -> {
                if(ex == null){
                    log.info("Payment Status event published successfully. Offset={}",result.getRecordMetadata().offset());
                }
                else{
                    log.error("Failed to publish Payment Status event for Order {}",event.getOrderId(),ex);
                }
            });
        } catch (Exception ex) {
            log.error("Unexpected error while sending Kafka message", ex);
            throw new RuntimeException("Unable to publish payment status event");
        }
    }
}
