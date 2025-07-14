package com.example.onlineticketreservationsystem.service;



import com.example.onlineticketreservationsystem.model.entity.RefreshToken;
import com.example.onlineticketreservationsystem.repository.RefreshTokenRepository;
import com.example.onlineticketreservationsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userInfoRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userInfoRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoRepository.findByUsername(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(60000))//10
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

}
