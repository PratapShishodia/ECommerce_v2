package com.ps.common.event;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotificationEvent {
    private Long userId;
    private String recipient;
    private String subject;
    private String message;
}