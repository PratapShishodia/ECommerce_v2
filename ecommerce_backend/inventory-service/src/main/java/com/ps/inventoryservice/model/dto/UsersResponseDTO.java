package com.ps.inventoryservice.model.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersResponseDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String mobileNumber;
    private String role;
}
