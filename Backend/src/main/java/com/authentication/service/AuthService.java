package com.authentication.service;

import org.springframework.stereotype.Service;

import com.authentication.dto.request.RegisterRequest;
import com.authentication.dto.response.RegisterResponse;


public interface AuthService {
 public RegisterResponse register(RegisterRequest request);
}
