package com.uni.kitcheniq.controller;

import com.uni.kitcheniq.dto.UserDTO;
import com.uni.kitcheniq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kitcheniq/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserDTO> getMyInfo(@RequestParam String userId){
        UserDTO user = userService.getUserbyId(userId);
        return ResponseEntity.ok(user);
    }
}
