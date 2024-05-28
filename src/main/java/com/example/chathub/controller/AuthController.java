package com.example.chathub.controller;

import com.example.chathub.model.AuthRequest;
import com.example.chathub.model.JwtResponse;
import com.example.chathub.model.User;
import com.example.chathub.model.UserRole;
import com.example.chathub.service.AuthService;
import com.example.chathub.service.JwtService;
import com.example.chathub.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthService authService;

    @Value("${application.security.cookie.expiration}")
    private int COOKIE_EXPIRATION;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        UserDetails user = authService.loadUserByUsername(authRequest.getEmail());
        if (user != null) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(user);
                ResponseCookie cookie = ResponseCookie.from("token", token)
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(COOKIE_EXPIRATION)
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                return JwtResponse.builder()
                        .token(token)
                        .build();
            }
        }
        throw new UsernameNotFoundException("Invalid email");
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
