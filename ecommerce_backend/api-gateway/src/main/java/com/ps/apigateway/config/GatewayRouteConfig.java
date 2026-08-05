package com.ps.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()

                .route("user-service", r -> r
                        .path("/api/user/**")
                        .uri("lb://USER-SERVICE"))

                .route("notification-service", r -> r
                        .path("/api/notification/**")
                        .uri("lb://NOTIFICATION-SERVICE"))

                .route("payment-service", r -> r
                        .path("/api/payment/**")
                        .uri("lb://PAYMENT-SERVICE"))

                .route("product-service", r -> r
                        .path("/api/product/**")
                        .uri("lb://PRODUCT-SERVICE"))

                .route("inventory-service", r -> r
                        .path("/api/inventory/**")
                        .uri("lb://INVENTORY-SERVICE"))

                .route("order-service", r -> r
                        .path("/api/order/**")
                        .uri("lb://ORDER-SERVICE"))

                .build();
    }
}