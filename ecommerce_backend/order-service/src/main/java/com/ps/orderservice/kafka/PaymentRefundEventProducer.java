package com.ps.orderservice.kafka;

import com.ps.common.event.InventoryStockUpdatesEvent;
import com.ps.common.event.PaymentRefundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentRefundEventProducer {
    private final KafkaTemplate<String, PaymentRefundEvent> kafkaTemplate;
    @Value("${spring.kafka.topics.payment}")
    private String paymentTopicName;

    public void sendPaymentRefundEvent(PaymentRefundEvent event){
        try {
            kafkaTemplate.send(paymentTopicName,event.getOrderId(),event).whenComplete(
                    (result,ex) -> {
                        if(ex == null){
                            log.info("Payment Refund Event Published for Order = {}. Offset={}",event.getOrderId(),result.getRecordMetadata().offset());
                        }
                        else {
                            log.error("Failed to publish Payment Refund Event for Order {}",event.getOrderId(),ex);
                        }
                    }
            );
        } catch (Exception e) {
            log.error("Unexpected error while sending Kafka message", e);
            throw new RuntimeException("Unable to publish Payment Refund event");
        }
    }
}
