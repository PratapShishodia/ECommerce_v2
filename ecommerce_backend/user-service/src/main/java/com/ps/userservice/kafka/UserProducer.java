package com.ps.userservice.kafka;

import com.ps.common.event.UserEvent;
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
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void sendUserEvent(UserEvent userEvent){
        try{
            kafkaTemplate.send(userTopic,userEvent.getUserId().toString() ,userEvent).whenComplete((result,ex) -> {
                if(ex == null){
                    log.info("User event published successfully. Offset={}",result.getRecordMetadata().offset());
                }
                else{
                    log.error("Failed to publish user event for user {}",userEvent.getUserId(),ex);
                }
            });
        } catch (Exception ex) {
            log.error("Unexpected error while sending Kafka message", ex);
            throw new RuntimeException("Unable to publish user event");
        }
    }
}
