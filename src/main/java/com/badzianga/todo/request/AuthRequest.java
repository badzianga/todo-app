package com.badzianga.todo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a well-formed email address")
    private String email;
    @Size(min = 3, max = 64, message = "Password must have 3-64 characters")
    private String password;
}
