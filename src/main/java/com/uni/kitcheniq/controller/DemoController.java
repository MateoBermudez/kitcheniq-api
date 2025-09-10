package com.uni.kitcheniq.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kitcheniq/api/v1")
public class DemoController {

    @PostMapping("/demo")
    public String welcome(){
        return "Welcome to KitchenIQ Application";
    }
}
