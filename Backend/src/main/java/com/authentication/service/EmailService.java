package com.authentication.service;


public interface EmailService {
    void sendVerificationEmail(String toEmail, String token);
}