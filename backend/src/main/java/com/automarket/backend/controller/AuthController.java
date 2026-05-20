package com.automarket.backend.controller;

import com.automarket.backend.dto.*;
import com.automarket.backend.model.User;
import com.automarket.backend.repository.UserRepository;
import com.automarket.backend.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .phone(req.getPhone())
                .city(req.getCity())
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getRole().name(), user.getFirstName()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        return userRepository.findByEmail(req.getEmail())
                .filter(user -> passwordEncoder.matches(req.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
                    return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getRole().name(), user.getFirstName()));
                })
                .orElse(ResponseEntity.status(401).body(null));
    }
}
