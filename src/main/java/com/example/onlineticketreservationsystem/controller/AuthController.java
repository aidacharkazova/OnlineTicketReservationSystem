package com.example.onlineticketreservationsystem.controller;

import com.example.onlineticketreservationsystem.dto.request.LoginRequest;
import com.example.onlineticketreservationsystem.dto.request.RefreshTokenRequest;
import com.example.onlineticketreservationsystem.dto.response.TokenResponse;
import com.example.onlineticketreservationsystem.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenUtil.generateAccessToken(request.getUsername());
        String refreshToken = jwtTokenUtil.generateRefreshToken(request.getUsername());

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (jwtTokenUtil.validateToken(refreshToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
            String newAccessToken = jwtTokenUtil.generateAccessToken(username);
            return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }
}
