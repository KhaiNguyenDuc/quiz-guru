package com.khai.quizguru.service;

import com.khai.quizguru.payload.request.RegisterRequest;
import com.khai.quizguru.model.User.User;
import com.khai.quizguru.payload.response.RegisterResponse;

public interface UserService {
    RegisterResponse createUser(RegisterRequest registerRequest);
}
