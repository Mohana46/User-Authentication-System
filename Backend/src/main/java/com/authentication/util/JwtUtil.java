package com.authentication.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Expiry times
    private final long ACCESS_EXPIRY = 1000 * 60 * 15;
    private final long REFRESH_EXPIRY = 1000L * 60 * 60 * 24 * 7;
    private final long VERIFY_EXPIRY = 1000 * 60 * 15;

    // GENERATE TOKENS
   

    public String generateAccessToken(String email) {
        return generateToken(email, ACCESS_EXPIRY);
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, REFRESH_EXPIRY);
    }

    public String generateVerificationToken(String email) {
        return generateToken(email, VERIFY_EXPIRY);
    }

    private String generateToken(String email, long expiry) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getKey(), SignatureAlgorithm.HS256) 
                .compact();
    }

   
    // EXTRACT EMAIL
  

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

  
    // VALIDATION
    

    public boolean isTokenValid(String token) {
        try {
            return !isExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()             
                .setSigningKey(getKey())         
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}