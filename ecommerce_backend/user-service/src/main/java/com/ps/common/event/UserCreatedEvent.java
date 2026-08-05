package com.ps.common.event;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreatedEvent {
    private Long userId;
    private String recipient;
    private String message;
    private String subject;
}
