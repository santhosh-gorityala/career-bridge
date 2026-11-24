package com.jobPortal.careerBridge.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "profile_id",nullable = false,unique = true)
    private UserProfile profile;

    @Column(name = "file_name",nullable = false,length = 255)
    private String fileName;

    @Column(name = "storage_key",nullable = false,length = 100)
    private String storageKey;

    @Column(name = "content_type",nullable = false,length = 100)
    private String contentType;

    @Column(name = "file_size",nullable = false)
    private Long fileSize;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updateAt;


    public Resume(
        UserProfile profile,
        String fileName,
        String storageKey,
        String contentType,
        Long fileSize,
        LocalDateTime createdAt,
        LocalDateTime updateAt
    ){
        this.profile = profile;
        this.fileName = fileName;
        this.storageKey = storageKey;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public void updateFile(
            String fileName,
            String storageKey,
            String contentType,
            Long fileSize
    ){
        this.fileName = fileName;
        this.storageKey = storageKey;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.updateAt = LocalDateTime.now();
    }


}
