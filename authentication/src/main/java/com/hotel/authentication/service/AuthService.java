package com.hotel.authentication.service;

import com.hotel.authentication.dto.LoginRequestDTO;
import com.hotel.authentication.dto.LoginResponseDTO;
import com.hotel.authentication.dto.RegisterRequestDTO;

public interface AuthService {
    String register(RegisterRequestDTO registerRequest);

    LoginResponseDTO login(LoginRequestDTO loginRequest);
}
