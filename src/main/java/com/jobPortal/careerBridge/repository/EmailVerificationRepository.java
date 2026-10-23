package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationToken,Long> {

    Optional<EmailVerificationToken>
    findByTokenHash(String tokenHash);

}
