package com.authentication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Pattern(
        regexp = "^[A-Z][a-zA-Z]{3,}\\s[A-Za-z]{1,}$",
        message = "Full name must be like 'John D' (First name capital, min 4 letters, last name min 1 letter)"
    )
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}