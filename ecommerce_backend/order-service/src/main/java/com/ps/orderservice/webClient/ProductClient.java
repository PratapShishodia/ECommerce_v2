package com.ps.orderservice.webClient;

import com.ps.orderservice.webClient.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ProductClient {

    private final WebClient productWebClient;

    public ProductDTO getProductById(Long productId) {
        return productWebClient.get()
                .uri("/api/product/{productId}", productId)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .block();
    }
}
