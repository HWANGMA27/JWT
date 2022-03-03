package com.tutorial.jwt.jwt;

import com.tutorial.jwt.entity.RefreshToken;
import com.tutorial.jwt.entity.User;
import com.tutorial.jwt.exception.RefreshTokenException;
import com.tutorial.jwt.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
public class RefreshTokenProvider {

    private final RefreshTokenService refreshTokenService;
    private final Long refreshTokenDuration;

    public RefreshTokenProvider(
            @Value("${jwt.refresh-expiration-in-seconds}") long refreshTokenExpirationSeconds,
            RefreshTokenService refreshTokenService) {
        this.refreshTokenDuration = refreshTokenExpirationSeconds * 1000;
        this.refreshTokenService = refreshTokenService;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDuration))
                .token(UUID.randomUUID().toString())
                .build();

        return refreshTokenService.saveRefreshToken(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenService.deleteExpiredToken(token);
            throw new RefreshTokenException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public void deleteByUser(User user) {
        refreshTokenService.deleteRefreshToken(user);
    }
}
