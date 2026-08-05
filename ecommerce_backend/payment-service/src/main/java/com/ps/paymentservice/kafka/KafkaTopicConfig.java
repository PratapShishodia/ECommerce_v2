package com.ps.paymentservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topics.notification}")
    private String notificationTopicName;

    @Value("${spring.kafka.topics.payment}")
    private String paymentTopicName;

    @Bean
    public NewTopic newTopic(){
        return TopicBuilder.name(paymentTopicName).partitions(3).replicas(1).build();
    }
    @Bean
    public NewTopic notificationTopic(){
        return TopicBuilder.name(notificationTopicName).partitions(3).replicas(1).build();
    }
}
