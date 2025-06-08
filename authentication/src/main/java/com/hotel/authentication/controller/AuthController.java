package com.hotel.authentication.controller;

import com.hotel.authentication.dto.LoginRequestDTO;
import com.hotel.authentication.dto.LoginResponseDTO;
import com.hotel.authentication.dto.RegisterRequestDTO;
import com.hotel.authentication.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequestDTO registerUser) {
        logger.info("Register request received for user: {}", registerUser.getUsername());
        String message = authService.register(registerUser);
        logger.info("Registration result: {}", message);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO user) {
        logger.info("Login attempt for user: {}", user.getUsername());
        LoginResponseDTO loginResponse = authService.login(user);
        logger.info("Login successful for user: {}", user.getUsername());
        return ResponseEntity.ok(loginResponse);
    }

}
