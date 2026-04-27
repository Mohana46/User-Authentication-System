package com.authentication.service;


import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String toEmail, String token) {

        String subject = "Verify Your Email";
        String content = loadTemplate(token);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setFrom("user.auth.sys@gmail.com"); 
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    private String loadTemplate(String token) {
        try {
            InputStream is = new ClassPathResource("templates/Email-Verification-Template.html").getInputStream();
            String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            String link = "http://localhost:8080/auth/verify?token=" + token;

            return html.replace("{{LINK}}", link);

        } catch (Exception e) {
            throw new RuntimeException("Template load failed");
        }
    }
}