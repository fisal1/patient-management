package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDto;
import com.pm.authservice.model.User;
import com.pm.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private UserServiece userServiece;
    private PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserServiece userServiece, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userServiece = userServiece;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    public Optional<String> authenticate(LoginRequestDto loginRequestDto) {
        Optional<String> token = userServiece.findByEmail(loginRequestDto.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequestDto.getPassword(),
                        u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(),u.getRole()));
        return token;
    }

    public Boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        }
        catch (JwtException e) {
            return false;
        }
    }
}
