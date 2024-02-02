package com.khai.quizguru.payload.request;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String password;
}
