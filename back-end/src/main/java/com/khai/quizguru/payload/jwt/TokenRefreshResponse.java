package com.khai.quizguru.payload.jwt;

import com.khai.quizguru.model.user.RefreshToken;
import com.khai.quizguru.model.user.User;
import lombok.Data;

@Data
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private User user;

    public TokenRefreshResponse(String accessToken, RefreshToken refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken.getToken();
        this.user = refreshToken.getUser();
    }

    // getters and setters
}