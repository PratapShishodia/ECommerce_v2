package com.ps.orderservice.repository;

import com.ps.orderservice.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrderRepo extends JpaRepository<Order,String> {
    Optional<Order> findById(String orderId);
    Page<Order> findByUserId(Long userId, Pageable pageable);
}
