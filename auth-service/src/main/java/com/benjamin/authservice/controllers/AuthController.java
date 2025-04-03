package com.benjamin.authservice.controllers;

import com.benjamin.authservice.dtos.request.LoginRequestDto;
import com.benjamin.authservice.dtos.request.RegisterRequest;
import com.benjamin.authservice.dtos.response.LoginResponseDto;
import com.benjamin.authservice.services.AuthService;
import com.benjamin.authservice.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication APIs")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenService tokenService;

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var accessToken = tokenService.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponseDto(accessToken));
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Register Api")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        var result = authService.register(request);

        return ResponseEntity.ok(result);
    }
}
