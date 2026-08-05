package com.ps.paymentservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class RefundRequestDTO {
    private String orderId;
    private Long paymentId;
    private BigDecimal refundAmount;
    private String reason;
}