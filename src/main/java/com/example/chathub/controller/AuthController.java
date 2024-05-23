package com.example.chathub.controller;

import com.example.chathub.model.User;
import com.example.chathub.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody User user) {
        User newUser = authService.findByEmail(user.getEmail());
        System.out.println(user);
        if (newUser.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<Void>(HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<Void>(HttpStatusCode.valueOf(400));
    }

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        User newUser = new User(user.getEmail(), passwordEncoder.encode(user.getPassword()));
        return authService.save(newUser);
    }

    @DeleteMapping("/logout")
    public String logout() {
        return "User logged out successfully";
    }
}
