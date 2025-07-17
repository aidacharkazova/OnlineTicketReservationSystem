package com.example.onlineticketreservationsystem.service;


import com.example.onlineticketreservationsystem.config.JwtUtil;
import com.example.onlineticketreservationsystem.model.entity.AppUser;
import com.example.onlineticketreservationsystem.model.entity.RefreshToken;
import com.example.onlineticketreservationsystem.repository.RefreshTokenRepository;
import com.example.onlineticketreservationsystem.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userInfoRepository;

    private final JwtUtil jwtUtil;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userInfoRepository, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userInfoRepository = userInfoRepository;
        this.jwtUtil = jwtUtil;
    }




    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found with username: " + username)))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(60000))//10
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public String refreshAccessToken(String refreshToken) {
        return findByToken(refreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(user -> jwtUtil.generateToken(userDetailsFromUser(user)))
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new sign in request");
        }
        return token;
    }
    private UserDetails userDetailsFromUser(AppUser user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // optional — won't affect token generation
                .authorities(user.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
    }


}
