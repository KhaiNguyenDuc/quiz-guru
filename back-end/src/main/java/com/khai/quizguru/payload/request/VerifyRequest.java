package com.khai.quizguru.payload.request;

import lombok.Data;

@Data
public class VerifyRequest {
    private String username;
    private String token;
}
