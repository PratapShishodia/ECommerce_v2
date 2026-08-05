package com.ps.paymentservice.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PaymentResponseDTO {
    private Long userId;
    private Long paymentId;
    private String orderId;
    private UUID transactionId;
    private BigDecimal amount;
    private String paymentCurrency;
    private String paymentMethod;
    private String status;
    private LocalDateTime paymentDate;
}
