package com.khai.quizguru.service;

import com.khai.quizguru.model.user.RefreshToken;

/**
 * Service interface of refresh token.
 */
public interface RefreshTokenService {
    RefreshToken createRefreshToken(String id);

    RefreshToken findByToken(String requestRefreshToken);

    RefreshToken deleteIfExpired(RefreshToken token);
}
