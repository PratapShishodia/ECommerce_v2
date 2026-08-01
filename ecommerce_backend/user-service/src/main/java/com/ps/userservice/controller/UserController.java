package com.ps.userservice.controller;

import com.ps.userservice.model.dto.UserRequestDTO;
import com.ps.userservice.model.dto.UserResponseDTO;
import com.ps.userservice.model.dto.common.LoginRequestDTO;
import com.ps.userservice.model.dto.common.LoginResponseDTO;
import com.ps.userservice.model.dto.common.RefreshRequest;
import com.ps.userservice.model.dto.common.UpdatePasswordDTO;
import com.ps.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<LoginResponseDTO> signup(@Valid @RequestBody UserRequestDTO requestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(requestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO){
        return ResponseEntity.accepted().body(userService.login(requestDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponseDTO> update(@RequestBody UserRequestDTO requestDTO){
        return ResponseEntity.accepted().body(userService.updateUser(requestDTO));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getLoggedIn(){
        return ResponseEntity.accepted().body(userService.getLoggedInUser());
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody RefreshRequest request){
        return ResponseEntity.accepted().body(userService.refreshToken(request));
    }

    @PutMapping("/resetPassword/sendOTP")
    public ResponseEntity<String> sendOTP(@RequestParam String email){
        userService.sendOTP(email);
        return ResponseEntity.accepted().body("OTP Send to Registered Email");
    }

    @GetMapping("/activate/{token}")
    public ResponseEntity<String> activateProfile(@PathVariable String token){
        userService.activateProfile(token);
        return ResponseEntity.accepted().body("Account Activated Successfully");
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody UpdatePasswordDTO passwordDTO){
        userService.changePassword(passwordDTO);
        return ResponseEntity.accepted().body("Password Updated Successfully");
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String email,@RequestBody UpdatePasswordDTO passwordDTO){
        userService.resetPassword(email,passwordDTO);
        return ResponseEntity.accepted().body("Password Update Successfully");
    }

    @PutMapping("/verifyOTP")
    public ResponseEntity<String> verifyOTP(@RequestParam String email,@RequestParam String otp){
        userService.verifyOTP(email,otp);
        return ResponseEntity.accepted().body("OTP Verified Successfully");
    }

}
