package com.khai.quizguru.service;

import com.khai.quizguru.model.user.VerificationToken;

/**
 * Service interface of verifyToken.
 */
public interface VerifyTokenService {

    VerificationToken findTokenByUser(String userId);

    Boolean verifyUser(String token, String username);
}
