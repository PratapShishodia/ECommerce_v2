package com.ps.userservice.util;

import com.ps.userservice.customExceptions.ResourceNotFoundException;
import com.ps.userservice.model.entity.Users;
import com.ps.userservice.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User","Email",email));
        return new CustomUserDetails(user);
    }
}
