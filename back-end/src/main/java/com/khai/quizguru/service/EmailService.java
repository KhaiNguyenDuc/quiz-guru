package com.khai.quizguru.service;

import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.EmailRequest;

public interface EmailService {

   void sendVerificationEmail(EmailRequest emailRequest);

    void sendResetPasswordEmail(EmailRequest emailRequest);

    void sendResetPasswordSuccessEmail(EmailRequest emailRequest);
}
