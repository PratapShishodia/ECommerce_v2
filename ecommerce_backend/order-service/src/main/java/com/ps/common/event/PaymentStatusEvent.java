package com.ps.common.event;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentStatusEvent {
    String orderId;
    String paymentStatus;
    Long paymentId;
}
