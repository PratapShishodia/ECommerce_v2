package com.ps.userservice.model.dto.common;

import com.ps.userservice.model.dto.UserResponseDTO;

public record LoginResponseDTO(String message, UserResponseDTO responseDTO, String refreshToken, String accessToken) {
}
