package com.benjamin.authservice.services;

import com.benjamin.authservice.dtos.request.ChangePasswordRequest;
import com.benjamin.authservice.dtos.request.PasswordResetRequest;
import com.benjamin.authservice.dtos.request.RegisterRequest;
import com.benjamin.authservice.enums.Role;
import com.benjamin.authservice.exceptions.SamePasswordException;
import com.benjamin.authservice.models.Uzer;
import com.benjamin.authservice.repositories.UserRepository;
import com.benjamin.authservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordService passwordService;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Uzer user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Email not register yet" + email);
        }

        Role role = user.getRole();
        String authorityName = "ROLE_" + role.name();
        Set<GrantedAuthority> grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(authorityName));
        return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

    public UUID register(RegisterRequest request) {
        var existingUser = userRepository.findByEmailAndPhoneNumber(request.getEmail(), request.getPhoneNumber());

        if (existingUser != null) {
            throw new RuntimeException("User already exists with this email address " + request.getEmail());
        }

        Uzer user = new Uzer();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(Role.PATIENT);

        //TODO: create email request

        user = userRepository.save(user);

        //double check
        userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Register failed"));

        return user.getId();
    }

    public boolean handleResetPassword(PasswordResetRequest request) {
        Uzer user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("Email not register yet" + request.getEmail());
        }

        String token = passwordService.generatePasswordResetToken(request.getEmail());

        //TODO: send to user email
        return true;
    }

    public boolean changePassword(ChangePasswordRequest request) {
        String email = passwordService.getEmailFromPasswordResetToken(request.getToken());
        Uzer user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email not register yet" + email);
        }
        if (user.getPassword().equals(request.getPassword())) {
            throw new SamePasswordException("Passwords do not match");
        }
        user.setPassword(passwordService.hashPassword(request.getPassword()));
        user = userRepository.save(user);

        //TODO: send email
        return true;
    }
}
