package com.ps.productservice.service;

import com.ps.productservice.model.dto.ProductRequestDTO;
import com.ps.productservice.model.dto.ProductResponseDTO;
import com.ps.productservice.model.dto.common.FilterRequestDTO;
import com.ps.productservice.model.dto.common.PageResponseDTO;

public interface ProductService {
    ProductResponseDTO findProductById(Long id);
    PageResponseDTO<ProductResponseDTO> findByCategory(int page_num,int page_size,String category);
    PageResponseDTO<ProductResponseDTO> findByBrandName(int page_num,int page_size,String brandName);
    PageResponseDTO<ProductResponseDTO> findAll(int page_num,int page_size);
    ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO);
    String deleteProductById(Long id);
    ProductResponseDTO updateProductById(Long id, ProductRequestDTO productRequestDTO);
    PageResponseDTO<ProductResponseDTO> findByFilter(int page_num,int page_size,FilterRequestDTO requestDTO);
}
