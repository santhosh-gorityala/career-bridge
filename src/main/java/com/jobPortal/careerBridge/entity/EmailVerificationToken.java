package com.jobPortal.careerBridge.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "token_hash",nullable = false,unique = true,length=255)
    private String tokenHash;

    @Column(name = "expires_at",nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    public EmailVerificationToken(
            User user,
            String tokenHash,
            LocalDateTime expiresAt,
            LocalDateTime createdAt
    ){
        this.user = user;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public void markUsed(){
        this.used = true;
    }


}
