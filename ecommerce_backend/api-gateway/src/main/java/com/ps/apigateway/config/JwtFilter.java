package com.ps.apigateway.config;

import com.ps.apigateway.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtFilter implements GlobalFilter {

    private final JWTUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Gateway JwtFilter executed");
        System.out.println("Path = " + path);
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println("Authorization = " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        System.out.println("Token valid = " + jwtUtil.isValid(token));

        System.out.println("Forwarding request...");
        if (!jwtUtil.isValid(token)) {

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private boolean isPublicEndpoint(String path) {

        return path.startsWith("/api/user/login")
                || path.startsWith("/api/user/signup")
                || path.startsWith("/api/user/verifyOTP")
                || path.startsWith("/api/user/sendOTP")
                || path.startsWith("/api/user/forgetPassword")
                || path.startsWith("/actuator");
    }
}