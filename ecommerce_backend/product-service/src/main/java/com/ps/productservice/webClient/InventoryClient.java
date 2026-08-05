package com.ps.productservice.webClient;

import com.ps.productservice.webClient.dto.InventoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class InventoryClient {

    private final WebClient inventoryWebClient;

    public Void createInventory(InventoryDTO requestDTO) {

        return inventoryWebClient.post()
                .uri("/inventory")
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Void updateStock(Long productId,
                                            InventoryDTO requestDTO) {

        return inventoryWebClient.put()
                .uri("/inventory/{productId}", productId)
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
