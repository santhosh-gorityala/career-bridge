package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.entity.RefreshToken;
import com.jobPortal.careerBridge.entity.User;
import com.jobPortal.careerBridge.exception.InvalidRefreshTokenException;
import com.jobPortal.careerBridge.repository.RefreshTokenRepository;
import com.jobPortal.careerBridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    @Value("${security.refresh-token.expiration-days}")
    private long refreshTokenExpirationDays;

    @Autowired
    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository
            ){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    private String generateRawToken(){
        byte[] randomBytes = new byte[32];

        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

    }

    private String hashToken(String rawToken) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    rawToken.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    public String createRefreshToken(User user){
        String rawToken = generateRawToken();
        String hashedToken = hashToken(rawToken);
        LocalDateTime now =  LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(refreshTokenExpirationDays);
        RefreshToken refreshToken = new RefreshToken(
          user,
          hashedToken,
          expiresAt,
          now
        );
        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }
    public RefreshToken validateRefreshToken(String rawToken) {

        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken =
                refreshTokenRepository.findByTokenHashForUpdate(tokenHash)
                        .orElseThrow(() ->
                                new InvalidRefreshTokenException(
                                        "Invalid refresh token"
                                )
                        );

        if(refreshToken.isRevoked()){
            throw new InvalidRefreshTokenException(
                    "Refresh token has been revoked"
            );
        }

        if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new InvalidRefreshTokenException(
                    "Refresh token has expired"
            );
        }


        return refreshToken;
    }


}
