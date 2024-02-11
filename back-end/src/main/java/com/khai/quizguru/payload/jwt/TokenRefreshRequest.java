package com.khai.quizguru.payload.jwt;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshRequest {

    @NotBlank(message = "RefreshToken can't be empty")
    private String refreshToken;

}