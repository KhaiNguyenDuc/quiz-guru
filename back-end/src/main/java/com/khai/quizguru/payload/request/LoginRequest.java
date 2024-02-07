package com.khai.quizguru.payload.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String email;
    private String password;
}
