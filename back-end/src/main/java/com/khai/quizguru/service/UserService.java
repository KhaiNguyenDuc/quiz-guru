package com.khai.quizguru.service;

import com.khai.quizguru.model.user.User;
import com.khai.quizguru.model.user.VerificationToken;
import com.khai.quizguru.payload.request.PasswordResetRequest;
import com.khai.quizguru.payload.request.ProfileRequest;
import com.khai.quizguru.payload.request.RegisterRequest;
import com.khai.quizguru.payload.response.RegisterResponse;
import com.khai.quizguru.payload.response.UserResponse;

/**
 * Service interface of user.
 */
public interface UserService {
    RegisterResponse createUser(RegisterRequest registerRequest);

    UserResponse getUserById(String id);

    UserResponse updateById(ProfileRequest profileRequest, String id);

    void createVerificationTokenForUser(User user);


    Boolean resendVerifyToken(String username);

    String sendResetPassword(PasswordResetRequest request);

    void resetPassword(PasswordResetRequest request);
}

