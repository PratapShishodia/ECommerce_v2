package com.ps.paymentservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStatusRequestDTO {
    private Long paymentId;
    private String status;
}
