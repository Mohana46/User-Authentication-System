package com.authentication.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authentication.dto.request.RegisterRequest;
import com.authentication.dto.response.RegisterResponse;
import com.authentication.entity.AuthProvider;
import com.authentication.entity.Role;
import com.authentication.entity.User;
import com.authentication.repository.UserRepository;
@Service
public class AuthServiceImpl implements AuthService {

	
	private final PasswordEncoder passwordEncoder;
	private UserRepository userRepo;
	
	public AuthServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepo) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.userRepo = userRepo;
	}

	@Override
	public RegisterResponse register(RegisterRequest request) {
		
		if (!request.getPassword().equals(request.getConfirmPassword())) {
			throw new IllegalArgumentException("Passwords do not match");
		}
		 Set<Role> roles = new HashSet<>();
	        roles.add(Role.USER);
		
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setVerified(false); 
		user.setAccountLocked(false);
		user.setFailedAttempts(0);
		user.setProvider(AuthProvider.LOCAL);
		user.setBlockUntil(null);
		user.setRoles(roles);
		userRepo.save(user);
		return new RegisterResponse("User registered successfully. Verify your email to activate your account.");
	}

}
