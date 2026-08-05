package com.ps.paymentservice.repository;

import com.ps.paymentservice.model.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(String orderId);
    Page<Payment> findByUserId(Long userId, Pageable pageable);
}
