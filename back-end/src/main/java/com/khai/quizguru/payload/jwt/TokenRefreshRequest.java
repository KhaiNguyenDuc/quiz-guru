package com.khai.quizguru.payload.jwt;

import lombok.Data;

@Data
public class TokenRefreshRequest {

    private String refreshToken;

}