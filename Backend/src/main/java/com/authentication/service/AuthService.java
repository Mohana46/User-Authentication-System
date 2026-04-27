package com.authentication.service;



import com.authentication.dto.request.LoginRequest;
import com.authentication.dto.request.RegisterRequest;
import com.authentication.dto.response.AuthResponse;
import com.authentication.dto.response.RegisterResponse;


public interface AuthService {
 public RegisterResponse register(RegisterRequest request);
 String verifyEmail(String token);
 AuthResponse login(LoginRequest request);
}
