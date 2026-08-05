package com.ps.productservice.model.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class FilterRequestDTO {
    private String productName;
    private String brandName;
    private String categoryName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
