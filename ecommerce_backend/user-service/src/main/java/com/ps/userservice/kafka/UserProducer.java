package com.ps.userservice.kafka;

import com.ps.common.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProducer {

    @Value("${spring.kafka.topics.user}")
    private String userTopic;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public void sendUserEvent(UserCreatedEvent userCreatedEvent){
        try{
            kafkaTemplate.send(userTopic, userCreatedEvent.getUserId().toString() , userCreatedEvent).whenComplete((result, ex) -> {
                if(ex == null){
                    log.info("User event published successfully. Offset={}",result.getRecordMetadata().offset());
                }
                else{
                    log.error("Failed to publish user event for user {}", userCreatedEvent.getUserId(),ex);
                }
            });
        } catch (Exception ex) {
            log.error("Unexpected error while sending Kafka message", ex);
            throw new RuntimeException("Unable to publish user event");
        }
    }
}
