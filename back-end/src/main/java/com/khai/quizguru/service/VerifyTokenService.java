package com.khai.quizguru.service;

import com.khai.quizguru.model.user.VerificationToken;

public interface VerifyTokenService {

    VerificationToken findTokenByUser(String userId);

    Boolean verifyUser(String token, String userId);
}
