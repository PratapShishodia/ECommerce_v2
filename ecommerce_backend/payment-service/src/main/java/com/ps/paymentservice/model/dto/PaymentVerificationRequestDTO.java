package com.ps.paymentservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVerificationRequestDTO {
    private String orderId;
    private Long paymentId;
}
