package com.ps.orderservice.kafka;

import com.ps.common.event.InventoryStockUpdatesEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryEventProducer {
    private final KafkaTemplate<String, InventoryStockUpdatesEvent> kafkaTemplate;
    @Value("${spring.kafka.topics.inventory}")
    private String inventoryTopicName;

    public void sendInventoryEvent(InventoryStockUpdatesEvent event){
        try {
            kafkaTemplate.send(inventoryTopicName, event.getOrderId(),event).whenComplete(
                    (result,ex) -> {
                        if(ex == null){
                            log.info("Inventory Update Stock Event Published for Order = {}. Offset={}",event.getOrderId(),result.getRecordMetadata().offset());
                        }
                        else {
                            log.error("Failed to publish Inventory Update Stock Event for Order {}",event.getOrderId(),ex);
                        }
                    }
            );
        } catch (Exception e) {
            log.error("Unexpected error while sending Kafka message", e);
            throw new RuntimeException("Unable to publish inventory update event");
        }
    }
}
