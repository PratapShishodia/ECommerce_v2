package com.ps.common.event;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEvent {
    private Long userId;
    private String recipient;
    private String message;
    private String subject;
}
