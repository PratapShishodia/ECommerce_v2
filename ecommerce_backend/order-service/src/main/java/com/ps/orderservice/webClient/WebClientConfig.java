package com.ps.orderservice.webClient;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced
    public WebClient inventoryWebClient(WebClient.Builder builder){
        return builder.baseUrl("http://INVENTORY-SERVICE").build();
    }

    @Bean
    @LoadBalanced
    public WebClient productWebClient(WebClient.Builder builder){
        return builder.baseUrl("http://PRODUCT-SERVICE").build();
    }
}
