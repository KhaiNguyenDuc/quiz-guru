package com.khai.quizguru.service;

import com.khai.quizguru.model.user.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String id);

    RefreshToken findByToken(String requestRefreshToken);

    RefreshToken verifyExpiration(RefreshToken token);
}
