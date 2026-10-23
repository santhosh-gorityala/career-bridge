package com.jobPortal.careerBridge.service;

public interface EmailService {
     void sendVerificationEmail(
            String email,
            String verificationToken
    );
}
