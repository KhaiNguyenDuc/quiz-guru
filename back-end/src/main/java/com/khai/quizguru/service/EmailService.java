package com.khai.quizguru.service;

import com.khai.quizguru.payload.request.EmailRequest;

/**
 * Service interface of email.
 */
public interface EmailService {

   void sendVerificationEmail(EmailRequest emailRequest);

    void sendResetPasswordEmail(EmailRequest emailRequest);

    void sendResetPasswordSuccessEmail(EmailRequest emailRequest);
}
