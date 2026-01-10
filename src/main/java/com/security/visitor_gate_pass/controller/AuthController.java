package com.security.visitor_gate_pass.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.visitor_gate_pass.dto.RegisterRequest;
import com.security.visitor_gate_pass.model.User;
import com.security.visitor_gate_pass.repository.UserRepository;
import com.security.visitor_gate_pass.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 🔑 SECURE ROLE ASSIGNMENT
        if (request.getRole() != null && !request.getRole().equalsIgnoreCase("USER")) {
            // Simple hardcoded secret for demo purposes
            // In prod, use environment variables or proper config
            if ("mySecretKey123".equals(request.getAdminSecret())) {
                user.setRole(request.getRole().toUpperCase());
            } else {
                return ResponseEntity.status(403).body("Invalid Admin Secret for restricted role");
            }
        } else {
            user.setRole("USER");
        }

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null ||
                !passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid credentials");
        }

        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(token);
    }
}
