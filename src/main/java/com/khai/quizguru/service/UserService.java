package com.khai.quizguru.service;

import com.khai.quizguru.payload.request.RegisterRequest;
import com.khai.quizguru.payload.response.RegisterResponse;
import com.khai.quizguru.payload.response.UserResponse;

public interface UserService {
    RegisterResponse createUser(RegisterRequest registerRequest);

    UserResponse getUserById(String id);
}
