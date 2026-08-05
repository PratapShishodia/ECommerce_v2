package com.ps.productservice.util;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String username;
    private String userId;
    private String email;
    private String roles;
}
