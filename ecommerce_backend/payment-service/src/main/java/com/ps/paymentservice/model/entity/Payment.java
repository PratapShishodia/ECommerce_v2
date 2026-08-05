package com.ps.paymentservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long paymentId;
    private Long userId;
    private String orderId;
    private UUID transactionId;
    private BigDecimal amount;
    private String paymentCurrency;
    private String paymentMethod;
    private String status;
    private LocalDateTime paymentDate;
}
