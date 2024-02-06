package com.khai.quizguru.service;

import com.khai.quizguru.model.user.User;
import com.khai.quizguru.model.user.VerificationToken;
import com.khai.quizguru.payload.request.PasswordResetRequest;
import com.khai.quizguru.payload.request.ProfileRequest;
import com.khai.quizguru.payload.request.RegisterRequest;
import com.khai.quizguru.payload.response.RegisterResponse;
import com.khai.quizguru.payload.response.UserResponse;

public interface UserService {
    RegisterResponse createUser(RegisterRequest registerRequest);

    UserResponse getUserById(String id);

    UserResponse updateById(ProfileRequest profileRequest, String id);

    VerificationToken createVerificationTokenForUser(User user);


    Boolean resendVerifyToken(String userId);

    void sendResetPassword(PasswordResetRequest request);

    void resetPassword(PasswordResetRequest request);
}

