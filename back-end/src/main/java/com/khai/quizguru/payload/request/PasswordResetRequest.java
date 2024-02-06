package com.khai.quizguru.payload.request;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
    private String password;
    private String token;
}
