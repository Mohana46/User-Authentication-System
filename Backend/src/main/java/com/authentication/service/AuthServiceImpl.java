package com.authentication.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authentication.dto.request.LoginRequest;
import com.authentication.dto.request.RegisterRequest;
import com.authentication.dto.response.AuthResponse;
import com.authentication.dto.response.RegisterResponse;
import com.authentication.entity.AuthProvider;
import com.authentication.entity.Role;
import com.authentication.entity.User;
import com.authentication.repository.UserRepository;
import com.authentication.util.JwtUtil;
@Service
public class AuthServiceImpl implements AuthService {

	
	private final PasswordEncoder passwordEncoder;
	private UserRepository userRepo;
	private final JwtUtil jwtUtil;
	private final EmailService emailService;
	

	public AuthServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepo, JwtUtil jwtUtil,
			EmailService emailService) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.userRepo = userRepo;
		this.jwtUtil = jwtUtil;
		this.emailService = emailService;
	}
	@Override
	public RegisterResponse register(RegisterRequest request) {

	    if (userRepo.existsByEmail(request.getEmail())) {
	        throw new RuntimeException("Email already registered");
	    }

	    if (!request.getPassword().equals(request.getConfirmPassword())) {
	        throw new IllegalArgumentException("Passwords do not match");
	    }

	  

	    User user = new User();
	    user.setName(request.getName());
	    user.setEmail(request.getEmail());
	    user.setPassword(passwordEncoder.encode(request.getPassword()));
	    user.setVerified(false);
	    user.setAccountLocked(false);
	    user.setFailedAttempts(0);
	    user.setProvider(AuthProvider.LOCAL);
	    user.setBlockUntil(null);
	    user.setRole(Role.USER);

	    userRepo.save(user);

	    String token = jwtUtil.generateVerificationToken(user.getEmail());

	    
	    try {
	        emailService.sendVerificationEmail(user.getEmail(), token);
	    } catch (Exception e) {
	        System.out.println("Email sending failed: " + e.getMessage());
	    }

	    return new RegisterResponse(
	            "User registered successfully. Please verify your email before login.");
	}
	
	
	@Override
	public String verifyEmail(String token) {
	    try {
	        if (!jwtUtil.isTokenValid(token)) {
	            throw new RuntimeException("Invalid or expired token");
	        }

	        String email = jwtUtil.extractEmail(token);

	        User user = userRepo.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        if (user.isVerified()) {
	            return "Already verified";
	        }

	        user.setVerified(true);
	        userRepo.save(user);

	        return "Email verified successfully";

	    } catch (Exception e) {
	        throw new RuntimeException("Verification failed");
	    }
	}
	
	@Override
	public AuthResponse login(LoginRequest request) {

	    User user = userRepo.findByEmail(request.getEmail())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	   
	    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	        throw new RuntimeException("Invalid credentials");
	    }

	    
	    if (!user.isVerified()) {
	        throw new RuntimeException("Please verify your email first");
	    }

	
	    if (user.isAccountLocked()) {
	        throw new RuntimeException("Account is locked");
	    }

	 
	    String accessToken = jwtUtil.generateAccessToken(user.getEmail());
	    String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

	    
	    long expiresIn = 900;

	    return new AuthResponse(
	            accessToken,
	            refreshToken,
	            user.getEmail(),
	            user.getRole().name(),
	            expiresIn
	    );
	}

}
