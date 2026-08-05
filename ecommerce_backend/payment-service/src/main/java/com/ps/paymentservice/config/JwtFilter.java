package com.ps.paymentservice.config;

import com.ps.paymentservice.util.JWTUtil;
import com.ps.paymentservice.util.UserDTO;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        UserDTO userDTO = null;
        String header = request.getHeader("Authorization");
        log.info("Header:{}", header);
        log.info("Request URL{}", request.getRequestURI());

        if(header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try{
                System.out.println("token from Try:"+token);
                userDTO = jwtUtil.extractClaims(token);
                System.out.println("userDTO:"+userDTO.getUsername());
            }catch (JwtException e){
                log.debug("Invalid token: {}",e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"Invalid or expired token\"}");
                return;
            }
        }
        System.out.println("UserName- "+userDTO.getUsername());
        System.out.println("UserName- "+userDTO.getEmail());
        if(userDTO != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDTO,
                            null,
                            AuthorityUtils.NO_AUTHORITIES
                    );
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }
}
