package com.khai.quizguru.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyRequest {

    @NotBlank(message = "Username can't be empty")
    private String username;

    @NotBlank(message = "Token can't be empty")
    private String token;
}
