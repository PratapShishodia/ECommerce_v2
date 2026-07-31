package com.ps.userservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_tbl")
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false,unique = true)
    private String email;
    private String password;
    @Column(nullable = false,unique = true)
    private String mobileNumber;
    private String roles;
    private String OTP;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean active;
    private boolean OTPVerified;
    private String activationToken;
    private LocalDateTime OTP_EXPIRATION;
    private LocalDateTime activationToken_EXPIRATION;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
