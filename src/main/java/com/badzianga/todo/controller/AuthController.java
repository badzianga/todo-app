package com.badzianga.todo.controller;

import com.badzianga.todo.request.AuthRequest;
import com.badzianga.todo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public void register(@Valid @RequestBody AuthRequest request) {
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        authService.register(request.getEmail(), request.getPassword());
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody AuthRequest request) {
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        return authService.login(request.getEmail(), request.getPassword());
    }
}
