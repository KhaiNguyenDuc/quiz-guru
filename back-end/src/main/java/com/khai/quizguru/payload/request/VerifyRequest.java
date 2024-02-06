package com.khai.quizguru.payload.request;

import lombok.Data;

@Data
public class VerifyRequest {
    private String userId;
    private String token;
}
