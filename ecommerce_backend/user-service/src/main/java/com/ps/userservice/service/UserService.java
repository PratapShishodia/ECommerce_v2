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
    boolean activateProfile(String activationToken);
    boolean changePassword(UpdatePasswordDTO updatePasswordDTO);
    boolean resetPassword(String email,UpdatePasswordDTO updatePasswordDTO);
    boolean verifyOTP(String email,String OTP);
}
