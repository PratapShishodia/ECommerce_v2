package com.ps.userservice.service.impl;

import com.ps.common.event.UserEvent;
import com.ps.userservice.kafka.UserProducer;
import com.ps.userservice.model.dto.UserRequestDTO;
import com.ps.userservice.model.dto.UserResponseDTO;
import com.ps.userservice.model.dto.common.LoginRequestDTO;
import com.ps.userservice.model.dto.common.LoginResponseDTO;
import com.ps.userservice.model.dto.common.RefreshRequest;
import com.ps.userservice.model.dto.common.UpdatePasswordDTO;
import com.ps.userservice.model.entity.Users;
import com.ps.userservice.model.mapper.UserDTOMapper;
import com.ps.userservice.repository.UserRepo;
import com.ps.userservice.service.UserService;
import com.ps.userservice.util.CustomUserDetailService;
import com.ps.userservice.util.CustomUserDetails;
import com.ps.userservice.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;
    private final CustomUserDetailService userDetailService;
    private final UserProducer userProducer;

    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.email(),requestDTO.password()));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if(userDetails == null){
            throw new RuntimeException("Some Error Occurred");
        }
        if(!userDetails.isEnabled()){
            throw new RuntimeException("Account not activated");
        }
        return new LoginResponseDTO("Login Successfully", UserDTOMapper.toDTO(userDetails.getUser()),jwtUtil.generateRefreshToken(userDetails.getUser()), jwtUtil.generateAccessToken(userDetails.getUser()));
    }

    @Override
    public LoginResponseDTO signUp(UserRequestDTO requestDTO) {
        if(userRepo.existsByEmail(requestDTO.getEmail())){
            throw new RuntimeException("Email already Registered! Try Login");
        }
        if(userRepo.existsByMobileNumber(requestDTO.getMobileNumber())){
            throw new RuntimeException("Mobile Number already Registered! Try Login");
        }
        Users user = UserDTOMapper.toEntity(requestDTO);
        user.setPassword(encoder.encode(requestDTO.getPassword()));
        user.setActivationToken(UUID.randomUUID().toString());
        if(requestDTO.getRoles() != null){
            user.setRoles(requestDTO.getRoles());
        }
        else {
            user.setRoles("USER");
        }
        user.setActivationToken_EXPIRATION(LocalDateTime.now().plusMinutes(15));
        Users savedUser = userRepo.save(user);
        String activationLink = "http://localhost:8080/api/user/activate?token=" + savedUser.getActivationToken();
        String subject = "Profile Activation Link";
        String body = """
                Hi %s,
                Welcome to MoneyManager! Please activate your account by clicking the link below:
                %s
                If you didn't create this account, you can safely ignore this email.
                """.formatted(savedUser.getName(), activationLink);

        UserEvent userEvent = UserEvent.builder()
                .userId(savedUser.getUserId())
                .subject(subject)
                .message(body)
                .recipient(savedUser.getEmail())
                .build();
        userProducer.sendUserEvent(userEvent);
        return new LoginResponseDTO("SignUp Successful. An activation mail is send to registered Email", UserDTOMapper.toDTO(savedUser), null,null);
    }

    @Override
    public UserResponseDTO getLoggedInUser() {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UserDTOMapper.toDTO(user.getUser());
    }

    @Override
    public UserResponseDTO updateUser(UserRequestDTO requestDTO) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users updatedUser = null;
        if (userDetails != null) {
            Users user = userDetails.getUser();
            if (userRepo.existsByEmailAndUserIdNot(requestDTO.getEmail(), user.getUserId())) {
                throw new RuntimeException("Email ID already registered");
            }
            if (userRepo.existsByMobileNumberAndUserIdNot(requestDTO.getMobileNumber(), user.getUserId())) {
                throw new RuntimeException("Mobile Number already registered");
            }
            if (requestDTO.getName() != null && !Objects.equals(user.getName(), requestDTO.getName())) {
                System.out.println("Update Last Name");
                user.setName(requestDTO.getName());
            }
            if (requestDTO.getMobileNumber() != null && !Objects.equals(user.getMobileNumber(), requestDTO.getMobileNumber())) {
                System.out.println("Update Mobile Number");
                user.setMobileNumber(requestDTO.getMobileNumber());
            }
            if (requestDTO.getEmail() != null && !Objects.equals(user.getEmail(), requestDTO.getEmail())) {
                System.out.println("Update Email");
                user.setEmail(requestDTO.getEmail());
            }
            updatedUser = userRepo.save(user);
        }
        if (updatedUser == null) {
            throw new RuntimeException("Unable to Update");
        }
        return UserDTOMapper.toDTO(updatedUser);
    }

    @Override
    public LoginResponseDTO refreshToken(RefreshRequest request) {
        String refreshToken = request.refreshToken();
        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        if (!jwtUtil.isValid(refreshToken, userDetails)) {
            throw new RuntimeException("Invalid Refresh Token");
        }
        Users user = ((CustomUserDetails) userDetails).getUser();
        return new LoginResponseDTO(
                "Token Refreshed Successfully",
                UserDTOMapper.toDTO(user),
                refreshToken,
                jwtUtil.generateAccessToken(user)
        );
    }

    @Override
    public void sendOTP(String email) {
        Users user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found with Email: " + email));
        String OTP = String.valueOf(100000 + new SecureRandom().nextInt(900000));
        user.setOTP(OTP);
        user.setOTP_EXPIRATION(LocalDateTime.now().plusMinutes(15));
        user.setOTPVerified(false);
        userRepo.save(user);
        String subject = "Forget Password";
        String body = "Your OTP to reset your password is <b>" + OTP + "</b>. It is valid for 15 minutes.<br><br>If you didn't request this, please ignore this email.\\nRegards,\\nE-Commerce Team";
        UserEvent userEvent = UserEvent.builder()
                .userId(user.getUserId())
                .message(body)
                .subject(subject)
                .recipient(user.getEmail())
                .build();
        userProducer.sendUserEvent(userEvent);
    }

    @Override
    public void activateProfile(String activationToken) {
        Users user = userRepo.findByActivationToken(activationToken).orElseThrow(() -> new RuntimeException("User Not Found"));
        if (user.getActivationToken_EXPIRATION().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Activation Link expired");
        user.setActive(true);
        user.setActivationToken("");
        userRepo.save(user);
    }

    @Override
    public void changePassword(UpdatePasswordDTO updatePasswordDTO) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails != null) {
            Users user = userDetails.getUser();
            if (!encoder.matches(updatePasswordDTO.oldPassword(), user.getPassword())) {
                throw new RuntimeException("Old Password do not match");
            }
            user.setPassword(encoder.encode(updatePasswordDTO.newPassword()));
            userRepo.save(user);
        }
    }

    @Override
    public void resetPassword(String email, UpdatePasswordDTO updatePasswordDTO) {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isOTPVerified())
            throw new RuntimeException("OTP verification required");

        user.setPassword(encoder.encode(updatePasswordDTO.newPassword()));

        user.setOTP(null);
        user.setOTP_EXPIRATION(null);
        user.setOTPVerified(false);

        userRepo.save(user);
    }

    @Override
    public void verifyOTP(String email, String OTP) {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOTP() == null) {
            throw new RuntimeException("Something Went Wrong!");
        }

        if (!user.getOTP().equals(OTP))
            throw new RuntimeException("Invalid OTP");

        if (user.getOTP_EXPIRATION().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");

        user.setOTPVerified(true);

        userRepo.save(user);
    }
}
