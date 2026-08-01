package com.ps.userservice.service;

import com.ps.userservice.model.dto.UserRequestDTO;
import com.ps.userservice.model.dto.UserResponseDTO;
import com.ps.userservice.model.dto.common.LoginRequestDTO;
import com.ps.userservice.model.dto.common.LoginResponseDTO;
import com.ps.userservice.model.dto.common.RefreshRequest;
import com.ps.userservice.model.dto.common.UpdatePasswordDTO;

public interface UserService {
    LoginResponseDTO login(LoginRequestDTO requestDTO);
    LoginResponseDTO
    signUp(UserRequestDTO requestDTO);
    UserResponseDTO getLoggedInUser();
    UserResponseDTO updateUser(UserRequestDTO requestDTO);
    LoginResponseDTO refreshToken(RefreshRequest request);
    void sendOTP(String email);
    void activateProfile(String activationToken);
    void changePassword(UpdatePasswordDTO updatePasswordDTO);
    void resetPassword(String email, UpdatePasswordDTO updatePasswordDTO);
    void verifyOTP(String email, String OTP);
}
