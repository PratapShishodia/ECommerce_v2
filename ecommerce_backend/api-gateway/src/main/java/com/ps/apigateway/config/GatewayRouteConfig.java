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

                .build();
    }
}