package com.example.chathub.controller;

import com.example.chathub.model.User;
import com.example.chathub.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public User login(@RequestParam String email, @RequestParam String password) {
        User user = authService.findByEmail(email);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @PostMapping
    public User signup(@RequestBody String email, @RequestParam String password) {
        User newUser = new User(email, passwordEncoder.encode(password));
        return authService.save(newUser);
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
