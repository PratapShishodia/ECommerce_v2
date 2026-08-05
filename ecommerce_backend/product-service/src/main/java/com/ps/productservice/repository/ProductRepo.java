package com.ps.productservice.repository;

import com.ps.productservice.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Page<Product> findByBrandNameAndActiveTrue(String brandName,Pageable pageable);
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByProductCategoryAndActiveTrue(String productCategory, Pageable pageable);
    Optional<Product> findProductByProductIdAndActiveTrue(Long id);
    @Query("select p from Product p where p.active = true and p.productName like concat('%',:productName,'%') and  p.brandName like concat('%',:brandName,'%') and p.productCategory like concat('%',:categoryName,'%') and p.productPrice between :minPrice and :maxPrice")
    Page<Product> findAsPerFilter(String productName, String brandName, String categoryName, BigDecimal minPrice,BigDecimal maxPrice, Pageable pageable);
}
// Select * from Products where productName like '%productName%' and brandName like '%brandName%' and category like '%category%' and price between 10 and 50