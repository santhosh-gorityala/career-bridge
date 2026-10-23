package com.jobPortal.careerBridge.service.impl;

import com.jobPortal.careerBridge.exception.EmailSendingException;
import com.jobPortal.careerBridge.service.EmailService;
import jakarta.validation.constraints.Email;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendVerificationEmail(String email,
                                      String verificationToken) {
        String verificationLink = "http://localhost:8080/api/v1/auth/verify-email?token="
                +verificationToken;
        try {
            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom("gorityalasanthosh800@gmail.com");
            message.setTo(email);
            message.setSubject("Verify your CareerBridge account");

            message.setText(
                    "Welcome to CareerBridge!\n\n" +
                            "Please verify your email address by clicking the link below:\n\n" +
                            verificationLink +
                            "\n\nThis link will expire in 30 minutes."
            );

            javaMailSender.send(message);
        } catch (MailException ex) {
            throw new EmailSendingException(
                    "Failed to send verification email",
                    ex
            );
        }




    }
}
