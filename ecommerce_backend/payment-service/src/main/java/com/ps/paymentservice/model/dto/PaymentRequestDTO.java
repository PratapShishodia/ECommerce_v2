package com.ps.paymentservice.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {
    private String orderId;
    private BigDecimal amount;
    private String paymentCurrency;
    private String paymentMethod;
}
