package com.khai.quizguru.service;

import com.khai.quizguru.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String id);

    RefreshToken findByToken(String requestRefreshToken);

    RefreshToken verifyExpiration(RefreshToken token);
}
