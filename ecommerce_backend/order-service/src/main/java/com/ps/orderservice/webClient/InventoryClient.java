package com.ps.orderservice.webClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class InventoryClient {

    private final WebClient inventoryWebClient;

    public Boolean checkAvailability(Long productId) {
        return inventoryWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/inventory/checkAvailability")
                        .queryParam("productId", productId)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}
