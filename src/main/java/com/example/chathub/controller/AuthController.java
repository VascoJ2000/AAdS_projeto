package com.example.chathub.controller;

import com.example.chathub.model.User;
import com.example.chathub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public User login(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                return user.get();
            }
        }
        return null;
    }

    @PostMapping
    public String signup(@RequestBody String email, @RequestParam String password) {
        User newUser = new User(email, password);
        userRepository.save(newUser);
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
