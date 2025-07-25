package com.example.onlineticketreservationsystem.controller;

import com.example.onlineticketreservationsystem.dto.request.LoginRequest;
import com.example.onlineticketreservationsystem.dto.request.RefreshTokenRequest;
import com.example.onlineticketreservationsystem.dto.request.SignUpRequest;
import com.example.onlineticketreservationsystem.dto.response.AuthResponse;
import com.example.onlineticketreservationsystem.dto.response.JwtResponse;
import com.example.onlineticketreservationsystem.service.impl.RefreshTokenService;
import com.example.onlineticketreservationsystem.service.impl.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        String newAccessToken = refreshTokenService.refreshAccessToken(request.getToken());
        return ResponseEntity.ok(new JwtResponse(newAccessToken));
    }
}
