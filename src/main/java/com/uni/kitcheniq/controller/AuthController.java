package com.uni.kitcheniq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kitcheniq/api/v1")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/auth/login")
    public String login(){
        return "Login from public API";
    }

    @PostMapping("/auth/register")
    public String register(){
        return "Register from public API";
    }
}

