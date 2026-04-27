package com.authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.dto.request.LoginRequest;
import com.authentication.dto.request.RegisterRequest;
import com.authentication.dto.response.AuthResponse;
import com.authentication.dto.response.RegisterResponse;
import com.authentication.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

 
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/verify")
    public void verifyEmail(@RequestParam String token, HttpServletResponse response) throws Exception {
        authService.verifyEmail(token);
        response.sendRedirect("http://localhost:3000/login"); 
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}