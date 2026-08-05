package com.ps.common.event;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRefundEvent {
    private String orderId;
    private Long paymentId;
    private BigDecimal refundAmount;
    private String reason;
}
