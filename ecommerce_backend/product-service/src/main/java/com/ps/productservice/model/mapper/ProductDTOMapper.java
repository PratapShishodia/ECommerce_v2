package com.ps.productservice.model.mapper;

import com.ps.productservice.model.dto.ProductRequestDTO;
import com.ps.productservice.model.dto.ProductResponseDTO;
import com.ps.productservice.model.entity.Product;

public class ProductDTOMapper {
    public static ProductResponseDTO toDTO(Product product) {
        return ProductResponseDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productCategory(product.getProductCategory())
                .brandName(product.getBrandName())
                .productImageUrl(product.getProductImageUrl())
                .quantity(product.getQuantity())
                .active(product.isActive())
                .build();
    }

    public static Product toEntity(ProductRequestDTO requestDTO) {
        return Product.builder()
                .productName(requestDTO.getProductName())
                .productDescription(requestDTO.getProductDescription())
                .productPrice(requestDTO.getProductPrice())
                .productCategory(requestDTO.getProductCategory())
                .brandName(requestDTO.getBrandName())
                .productImageUrl(requestDTO.getProductImageUrl())
                .active(requestDTO.isActive())
                .quantity(requestDTO.getQuantity())
                .build();
    }
}
