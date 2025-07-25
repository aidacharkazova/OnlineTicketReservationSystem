package com.example.onlineticketreservationsystem.service.impl;

import com.example.onlineticketreservationsystem.config.JwtUtil;
import com.example.onlineticketreservationsystem.dto.request.LoginRequest;
import com.example.onlineticketreservationsystem.dto.request.SignUpRequest;
import com.example.onlineticketreservationsystem.dto.response.AuthResponse;
import com.example.onlineticketreservationsystem.model.entity.AppUser;
import com.example.onlineticketreservationsystem.model.entity.RefreshToken;
import com.example.onlineticketreservationsystem.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                       CustomUserDetailsService userDetailsService, RefreshTokenService refreshTokenService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthResponse signUp(SignUpRequest request) {
        AppUser user = new AppUser();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(List.of("USER"));
        userRepo.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String accessToken = jwtUtil.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String accessToken = jwtUtil.generateToken(userDetails);


        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getEmail());

        return new AuthResponse(accessToken, refreshToken.getToken());
    }
}

