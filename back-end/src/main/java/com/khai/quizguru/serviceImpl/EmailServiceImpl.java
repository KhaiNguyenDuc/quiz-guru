package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.payload.request.EmailRequest;
import com.khai.quizguru.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * The EmailServiceImpl class provides implementation for sending emails asynchronously.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    /**
     * Sends a verification email asynchronously.
     * @param emailRequest The EmailRequest object containing email details
     */
    @Async
    @Override
    public void sendVerificationEmail(EmailRequest emailRequest) {
        try{
            String templateName = "verification.html";
            Context context = new Context();
            context.setVariable("message", emailRequest.getBody());
            MimeMessage message = constructEmailHtmlMessage(emailRequest, context, templateName);
            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Email Service: Send verify mail fail. Message: " + e.getMessage());
        }
    }

    /**
     * Sends a reset password email asynchronously.
     * @param emailRequest The EmailRequest object containing email details
     */
    @Async
    @Override
    public void sendResetPasswordEmail(EmailRequest emailRequest) {
        try{
            String templateName = "password-reset.html";
            Context context = new Context();
            context.setVariable("message", emailRequest.getBody());
            MimeMessage message = constructEmailHtmlMessage(emailRequest, context, templateName);
            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Email Service: Send password reset mail fail. Message: " + e.getMessage());
        }
    }

    /**
     * Sends a reset password success email asynchronously.
     * @param emailRequest The EmailRequest object containing email details
     */
    @Async
    @Override
    public void sendResetPasswordSuccessEmail(EmailRequest emailRequest) {
        try{
            String templateName = "template.html";
            Context context = new Context();
            context.setVariable("message", emailRequest.getBody());
            MimeMessage message = constructEmailHtmlMessage(emailRequest, context, templateName);
            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Email Service: Send password reset success mail fail. Message: " + e.getMessage());
        }
    }

    /**
     * Constructs a MimeMessage for sending HTML email.
     * @param emailRequest The EmailRequest object containing email details
     * @param context The Thymeleaf context object for email template processing
     * @param templateName The name of the Thymeleaf template for the email content
     * @return The constructed MimeMessage
     * @throws MessagingException if an error occurs while constructing the MimeMessage
     */
    public MimeMessage constructEmailHtmlMessage(EmailRequest emailRequest, Context context, String templateName) throws MessagingException {
        MimeMessage  mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setTo(emailRequest.getTo());
        helper.setSubject(emailRequest.getSubject());
        String htmlContent = templateEngine.process(templateName, context);
        helper.setText(htmlContent, true);
        return mimeMessage;

    }
}
