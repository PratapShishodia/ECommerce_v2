package com.ps.productservice.service.impl;

import com.ps.productservice.customExceptions.ResourceNotFoundException;
import com.ps.productservice.model.dto.ProductRequestDTO;
import com.ps.productservice.model.dto.ProductResponseDTO;
import com.ps.productservice.model.dto.common.FilterRequestDTO;
import com.ps.productservice.model.dto.common.PageResponseDTO;
import com.ps.productservice.model.dto.feign.InventoryRequestDTO;
import com.ps.productservice.model.entity.Product;
import com.ps.productservice.model.mapper.ProductDTOMapper;
import com.ps.productservice.repository.ProductRepo;
import com.ps.productservice.service.ProductService;
import com.ps.productservice.webClient.InventoryClient;
import com.ps.productservice.webClient.dto.InventoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepo  productRepo;
    private final InventoryClient inventoryClient;

    @Override
    public ProductResponseDTO findProductById(Long id) {
        return ProductDTOMapper.toDTO(productRepo.findProductByProductIdAndActiveTrue(id).orElseThrow(() -> new ResourceNotFoundException("Product","ProductId",String.valueOf(id))));
    }

    @Override
    public PageResponseDTO<ProductResponseDTO> findByCategory(int page_num, int page_size, String category) {
        Pageable pageable = PageRequest.of(page_num, page_size);
        Page<Product> productPage = productRepo.findByProductCategoryAndActiveTrue(category, pageable);
        List<ProductResponseDTO> productList = productPage.getContent().stream().map(ProductDTOMapper::toDTO).toList();
        PageResponseDTO<ProductResponseDTO> response = new PageResponseDTO<>();
        response.setContent(productList);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalElements(productPage.getTotalElements());
        response.setTotalPages(productPage.getTotalPages());
        response.setLastPage(productPage.isLast());
        return response;
    }

    @Override
    public PageResponseDTO<ProductResponseDTO> findByBrandName(int page_num, int page_size, String brandName) {
        Pageable pageable = PageRequest.of(page_num, page_size);
        Page<Product> productPage = productRepo.findByBrandNameAndActiveTrue(brandName, pageable);
        List<ProductResponseDTO> productList = productPage.getContent().stream().map(ProductDTOMapper::toDTO).toList();
        PageResponseDTO<ProductResponseDTO> response = new PageResponseDTO<>();
        response.setContent(productList);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalElements(productPage.getTotalElements());
        response.setTotalPages(productPage.getTotalPages());
        response.setLastPage(productPage.isLast());
        return response;
    }

    @Override
    public PageResponseDTO<ProductResponseDTO> findAll(int page_num, int page_size) {
        Pageable pageable = PageRequest.of(page_num, page_size);
        Page<Product> productPage = productRepo.findByActiveTrue(pageable);
        List<ProductResponseDTO> productList = productPage.getContent().stream().map(ProductDTOMapper::toDTO).toList();
        PageResponseDTO<ProductResponseDTO> response = new PageResponseDTO<>();
        response.setContent(productList);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalElements(productPage.getTotalElements());
        response.setTotalPages(productPage.getTotalPages());
        response.setLastPage(productPage.isLast());
        return response;
    }

    @Override
    @Transactional
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {
        Product product = ProductDTOMapper.toEntity(productRequestDTO);
        Product savedProduct = productRepo.save(product);
        inventoryClient.createInventory(InventoryDTO.builder().productId(savedProduct.getProductId()).quantity(savedProduct.getQuantity()).warehouseLocation(productRequestDTO.getWarehouseLocation()).build());
        return ProductDTOMapper.toDTO(savedProduct);
    }

    @Override
    @Transactional
    public String deleteProductById(Long id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product","ProductId",String.valueOf(id)));
        return "Product deleted successfully";
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProductById(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product","ProductId",String.valueOf(id)));
        if(productRequestDTO.getProductName() != null && !productRequestDTO.getProductName().isEmpty()){
            product.setProductName(productRequestDTO.getProductName());
        }
        if(productRequestDTO.getProductDescription() != null && !productRequestDTO.getProductDescription().isEmpty()){
            product.setProductDescription(productRequestDTO.getProductDescription());
        }
        if(productRequestDTO.getProductPrice() != null){
            product.setProductPrice(productRequestDTO.getProductPrice());
        }
        if(productRequestDTO.getProductCategory() != null && !productRequestDTO.getProductCategory().isEmpty()){
            product.setProductCategory(productRequestDTO.getProductCategory());
        }
        if(productRequestDTO.getBrandName() != null && !productRequestDTO.getBrandName().isEmpty()){
            product.setBrandName(productRequestDTO.getBrandName());
        }
        if(productRequestDTO.getProductImageUrl() != null && !productRequestDTO.getProductImageUrl().isEmpty()){
            product.setProductImageUrl(productRequestDTO.getProductImageUrl());
        }
        if(productRequestDTO.getQuantity() != null){
            product.setQuantity(product.getQuantity()+ productRequestDTO.getQuantity());
        }
        inventoryClient.updateStock(id, InventoryDTO.builder().productId(id).quantity(product.getQuantity()).warehouseLocation(productRequestDTO.getWarehouseLocation()).build());
        return ProductDTOMapper.toDTO(productRepo.save(product));
    }

    @Override
    public PageResponseDTO<ProductResponseDTO> findByFilter(int page_num, int page_size,FilterRequestDTO requestDTO) {
        Pageable pageable = PageRequest.of(page_num, page_size);
        Page<Product> productPage = productRepo.findAsPerFilter(requestDTO.getProductName(),requestDTO.getBrandName(),requestDTO.getCategoryName(),requestDTO.getMinPrice(),requestDTO.getMaxPrice(),pageable);
        List<ProductResponseDTO> productList = productPage.getContent().stream().map(ProductDTOMapper::toDTO).toList();
        PageResponseDTO<ProductResponseDTO> response = new PageResponseDTO<>();
        response.setContent(productList);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalElements(productPage.getTotalElements());
        response.setTotalPages(productPage.getTotalPages());
        response.setLastPage(productPage.isLast());
        return response;
    }
}
