package com.ps.userservice.model.mapper;

import com.ps.userservice.model.dto.UserRequestDTO;
import com.ps.userservice.model.dto.UserResponseDTO;
import com.ps.userservice.model.entity.Users;

public class UserDTOMapper {
    public static Users toEntity(UserRequestDTO requestDTO){
        return Users.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .mobileNumber(requestDTO.getMobileNumber())
                .roles(requestDTO.getRoles())
                .build();
    }

    public static UserResponseDTO toDTO(Users user){
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .build();
    }
}
