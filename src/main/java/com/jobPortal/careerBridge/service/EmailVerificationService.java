package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.entity.EmailVerificationToken;
import com.jobPortal.careerBridge.entity.User;
import com.jobPortal.careerBridge.exception.InvalidEmailVerificationTokenException;
import com.jobPortal.careerBridge.repository.EmailVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    @Value("${security.email-verification-token.expiration-minutes}")
    private long emailVerificationTokenExpirationMinutes;
    @Autowired
    public EmailVerificationService(
            EmailVerificationRepository emailVerificationRepository
    ){
        this.emailVerificationRepository = emailVerificationRepository;

    }

    private String generateRawVerificationToken(){
    byte[] randomBytes = new byte[32];

    secureRandom.nextBytes(randomBytes);

    return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(randomBytes);
    }

    private String hashToken(String rawToken){
        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    rawToken.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    e
            );
        }
    }

    public String createVerificationToken(
            User user
    ){
        String rawToken =
                generateRawVerificationToken();
        String hashedToken =
                hashToken(rawToken);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(emailVerificationTokenExpirationMinutes);
        EmailVerificationToken emailVerification =
                new EmailVerificationToken(
                        user,
                        hashedToken,
                        expiresAt,
                        now
                );
        emailVerificationRepository.save(emailVerification);
        return rawToken;
    }

    public EmailVerificationToken validateVerficationToken(
            String rawToken
    ){
        String hashedToken = hashToken(rawToken);
        EmailVerificationToken emailVerificationToken =
                emailVerificationRepository.
                findByTokenHash(hashedToken).orElseThrow( () ->
                        new InvalidEmailVerificationTokenException("Invalid verfication token")
                );

        if(emailVerificationToken.isUsed()){
            throw new InvalidEmailVerificationTokenException(
                    "Verification token has already been used"
            );
        }

        if(emailVerificationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new InvalidEmailVerificationTokenException(
                    "Verfication token has expired"
            );
        }


        return emailVerificationToken;
    }

    @Transactional
    public User verifyEmail(String rawToken){
        EmailVerificationToken emailVerificationToken =
                validateVerficationToken(rawToken);
        User user = emailVerificationToken.getUser();
        user.verifyEmail();
        emailVerificationToken.markUsed();
        return user;
    }


}
