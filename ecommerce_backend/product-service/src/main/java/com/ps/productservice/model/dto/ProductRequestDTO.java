package com.ps.productservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {
    @NotBlank(message = "Product Name is Required")
    private String productName;
    @NotBlank(message = "Product Description is Required")
    private String productDescription;
    private BigDecimal productPrice;
    @NotBlank(message = "Product Category is Required")
    private String productCategory;
    @NotBlank(message = "Brand Name is Required")
    private String brandName;
    private Integer quantity;
    private String productImageUrl;
    private boolean active;
    private String warehouseLocation;
}
