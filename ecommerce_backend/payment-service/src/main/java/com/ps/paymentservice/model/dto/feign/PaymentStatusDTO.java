package com.ps.paymentservice.model.dto.feign;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStatusDTO {
    private Long paymentId;
    private String status;
}
