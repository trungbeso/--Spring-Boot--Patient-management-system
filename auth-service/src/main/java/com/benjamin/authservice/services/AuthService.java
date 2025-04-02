package com.benjamin.authservice.services;

import com.benjamin.authservice.dtos.LoginRequestDto;
import com.benjamin.authservice.dtos.LoginResponseDto;
import com.benjamin.authservice.models.Uzer;
import com.benjamin.authservice.repositories.UserRepository;
import com.benjamin.authservice.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Optional<String> authenticate (LoginRequestDto request) {
        Optional<String> token = userRepository
                .findByEmail(request.getEmail())
                .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));
        return token;
    }

    public boolean validateToken (String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
