package com.authentication.dto.response;

public class AuthResponse { 
	private String accessToken; 
	private String refreshToken; 
	private String email; 
	private String role; 
	private long expiresIn; 
	public AuthResponse(String accessToken, String refreshToken, String email, String role, long expiresIn) 
	{ 
	this.accessToken = accessToken;
	this.refreshToken = refreshToken;
	this.email = email; 
	this.role = role;
	this.expiresIn = expiresIn; 
	} 
}