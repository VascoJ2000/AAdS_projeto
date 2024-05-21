package com.example.chathub.controller;

import com.example.chathub.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping
    public User login(@RequestParam String email, @RequestParam String password) {
        return new User(email);
    }

    @PostMapping
    public String signup() {
        return "User added successfully";
    }

    @DeleteMapping
    public String logout() {
        return "User logged out successfully";
    }

    @GetMapping("/token")
    public String getToken() {
        return "Login successful";
    }
}
