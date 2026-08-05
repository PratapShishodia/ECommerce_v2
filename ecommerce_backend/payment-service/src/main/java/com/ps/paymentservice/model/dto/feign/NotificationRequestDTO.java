package com.ps.paymentservice.model.dto.feign;
import lombok.*;


@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
    private Long userId;
    private String recipient;
    private String subject;
    private String message;
}
