package com.authentication.dto.request;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RegisterRequest {

    @NotBlank
    @Pattern(
        regexp = "^[A-Z][a-zA-Z]{3,}\\s[A-Za-z]{1,}$",
        message = "Full name must be like 'John D' (First name capital, min 4 letters, last name min 1 letter)"
    )
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
        message = "Password must be 8+ chars, include uppercase, lowercase, number and special character"
    )
    private String password;
    
    @Transient
    private String confirmPassword;
}