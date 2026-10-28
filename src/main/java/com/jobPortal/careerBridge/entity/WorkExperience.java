package com.jobPortal.careerBridge.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="work_experiences")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "profile_id",nullable = false)
    private UserProfile profile;

    @Column(name = "company_name",nullable = false,length = 255)
    private String companyName;

    @Column(name = "job_title",nullable = false,length = 255)
    private String jobTitle;

    @Column(name = "employment_type",nullable = false,length = 50)
    private String employmentType;

    @Column(name = "start_date",nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "currently_working",nullable = false)
    private boolean isCurrentyWorking;

    @Column(nullable = false)
    private String description;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;


    public WorkExperience(
        UserProfile profile,
        String companyName,
        String jobTitle,
        String employmentType,
        LocalDate startDate,
        LocalDate endDate,
        boolean isCurrentyWorking,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        this.profile = profile;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.employmentType = employmentType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrentyWorking = isCurrentyWorking;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateMyExperience(
            String companyName,
            String jobTitle,
            String employmentType,
            LocalDate startDate,
            LocalDate endDate,
            boolean isCurrentyWorking,
            String description
    ){
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.employmentType = employmentType;
        this.startDate = startDate;
        this.endDate =endDate;
        this.isCurrentyWorking = isCurrentyWorking;
        this.description = description;
    }

}
