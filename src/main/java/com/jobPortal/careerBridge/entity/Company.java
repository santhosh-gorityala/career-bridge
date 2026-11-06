package com.jobPortal.careerBridge.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "owner_user_id",nullable = false)
    private User owner;

    @Column(nullable = false,length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String website;

    @Column(length = 100)
    private String industry;

    @Column(name = "company_size",length = 50)
    private String companySize;

    @Column(length = 255)
    private String location;

    @Column(name = "logo_storage_key",length = 500)
    private String logoStorageKey;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;


    public Company(
         User owner,
         String name,
         String description,
         String website,
         String industry,
         String companySize,
         String location,
         String logoStorageKey,
         LocalDateTime createdAt,
         LocalDateTime updatedAt
    ){
        this.owner= owner;
        this.name = name;
        this.description = description;
        this.website = website;
        this.industry = industry;
        this.companySize = companySize;
        this.location = location;
        this.logoStorageKey = logoStorageKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update(
            String name,
            String description,
            String website,
            String industry,
            String companySize,
            String location
    ){
        this.name = name;
        this.description = description;
        this.website = website;
        this.industry = industry;
        this.companySize = companySize;
        this.location = location;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateLogo(
            String logoStorageKey
    ){
        this.logoStorageKey = logoStorageKey;
        this.updatedAt = LocalDateTime.now();
    }



}
