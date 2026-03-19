package com.badzianga.todo.controller;

import com.badzianga.todo.exception.EmailAlreadyUsedException;
import com.badzianga.todo.exception.InvalidEmailOrPasswordException;
import com.badzianga.todo.request.AuthRequest;
import com.badzianga.todo.response.ApiResponse;
import com.badzianga.todo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody AuthRequest request) {
        try {
            authService.register(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (EmailAlreadyUsedException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            String token = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(new ApiResponse("Success", token));
        } catch (InvalidEmailOrPasswordException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }
}
