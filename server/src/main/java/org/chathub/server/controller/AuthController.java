package org.chathub.server.controller;

import org.chathub.server.model.AuthRequest;
import org.chathub.server.model.User;
import org.chathub.server.model.UserRole;
import org.chathub.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        User user = userService.findByEmail(authRequest.getEmail());
        if (user != null) {
            if (user.getPassword().equals(authRequest.getPassword())) {
                return ResponseEntity.ok("User logged in successfully");
            }
            return ResponseEntity.ok("Password does not match!");
        }
        return ResponseEntity.badRequest().body("User not found!");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthRequest authRequest) {
        if(userService.findByEmail(authRequest.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }
        User user = new User(authRequest.getEmail(), authRequest.getPassword(), UserRole.USER);
        userService.save(user);
        return ResponseEntity.ok("User Added Successfully");
    }
}
