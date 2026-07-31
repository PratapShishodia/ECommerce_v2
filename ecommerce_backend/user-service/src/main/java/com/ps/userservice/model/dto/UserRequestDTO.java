package com.ps.userservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO {
    @NotBlank(message = "Name is Required")
    private String name;
    @Email(message = "A Valid Email is Required")
    private String email;
    private String password;
    @NotBlank(message = "Mobile Number is Required")
    private String mobileNumber;
    private String roles;
}
