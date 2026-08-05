package com.ps.orderservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.topics.inventory}")
    private String inventoryTopicName;
    @Value("${spring.kafka.topics.payment}")
    private String paymentTopicName;


    @Bean
    public NewTopic inventoryTopic(){
        return TopicBuilder.name(inventoryTopicName).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic paymentTopic(){
        return TopicBuilder.name(paymentTopicName).partitions(3).replicas(1).build();
    }

}
