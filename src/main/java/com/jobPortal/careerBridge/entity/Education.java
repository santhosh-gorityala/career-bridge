package com.jobPortal.careerBridge.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "educations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "profile_id",nullable = false)
    private UserProfile profile;

    @Column(nullable = false,length = 255)
    private String degree;

    @Column(nullable = false,length = 255)
    private String institution;

    @Column(name = "field_of_study",length = 255)
    private String fieldOfStudy;

    @Column(name = "start_date",nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "currently_studying",nullable = false)
    private boolean isCurrentlyStudying;

    @Column(length = 50)
    private String grade;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Education(
           UserProfile profile,
           String degree,
           String institution,
           String fieldOfStudy,
           LocalDate startDate,
           LocalDate endDate,
           boolean isCurrentlyStudying,
           String grade,
           String description,
           LocalDateTime createdAt,
           LocalDateTime updatedAt
    ){
     this.profile = profile;
     this.degree = degree;
     this.institution = institution;
     this.fieldOfStudy = fieldOfStudy;
     this.startDate = startDate;
     this.endDate = endDate;
     this.isCurrentlyStudying = isCurrentlyStudying;
     this.grade = grade;
     this.description = description;
     this.createdAt = createdAt;
     this.updatedAt = updatedAt;
    }

    public void update(
            String degree,
            String institution,
            String fieldOfStudy,
            LocalDate startDate,
            LocalDate endDate,
            boolean isCurrentlyStudying,
            String grade,
            String description
    ){
        this.degree = degree;
        this.institution = institution;
        this.fieldOfStudy = fieldOfStudy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrentlyStudying = isCurrentlyStudying;
        this.grade = grade;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

}
