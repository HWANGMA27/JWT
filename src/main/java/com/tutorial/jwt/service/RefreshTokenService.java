package com.tutorial.jwt.service;

import com.tutorial.jwt.entity.RefreshToken;
import com.tutorial.jwt.entity.User;
import com.tutorial.jwt.exception.RefreshTokenException;
import com.tutorial.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(readOnly = true)
    public RefreshToken findUserByRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new RefreshTokenException(token, "Refresh token is not in database!"));
    }

    public RefreshToken saveRefreshToken(RefreshToken token){
        return refreshTokenRepository.save(token);
    }

    public void deleteExpiredToken(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    public void deleteRefreshToken(User user) {
        refreshTokenRepository.deleteRefreshTokenByUser(user);
    }
}
