package com.uni.kitcheniq.controller;

import com.uni.kitcheniq.dto.AuthResponse;
import com.uni.kitcheniq.dto.LoginRequest;
import com.uni.kitcheniq.dto.RegisterRequest;
import com.uni.kitcheniq.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kitcheniq/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService AuthService;

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest Request) {
        return ResponseEntity.ok(AuthService.login(Request));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest Request) {
        return ResponseEntity.ok(AuthService.register(Request));
    }
}

