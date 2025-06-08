package com.hotel.authentication.service;

import com.hotel.authentication.dto.LoginRequestDTO;
import com.hotel.authentication.dto.LoginResponseDTO;
import com.hotel.authentication.dto.RegisterRequestDTO;
import com.hotel.authentication.exception.UserAlreadyExistsException;
import com.hotel.authentication.exception.UserNotFoundException;
import com.hotel.authentication.entity.User;
import com.hotel.authentication.repository.UserRepository;
import com.hotel.authentication.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String register(RegisterRequestDTO userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            logger.warn("Registration failed: Username '{}' already exists", userDto.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }

        if ("ADMIN".equalsIgnoreCase(userDto.getRole()) && userRepository.existsByRole("ADMIN")) {
            logger.warn("Registration failed: Admin already exists, cannot register another admin");
            throw new UserAlreadyExistsException("An admin already exists. Only one admin allowed.");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());

        userRepository.save(user);
        logger.info("User '{}' registered successfully with role '{}'", userDto.getUsername(), userDto.getRole());

        return "User registered successfully!";
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.warn("Login failed: Username '{}' not found", request.getUsername());
                    return new UserNotFoundException("Invalid username or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Login failed: Invalid password for username '{}'", request.getUsername());
            throw new UserNotFoundException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user);
        logger.info("User '{}' logged in successfully", request.getUsername());

        return new LoginResponseDTO("Login successful!", token);
    }
}
