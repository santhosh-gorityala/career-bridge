package com.jobPortal.careerBridge.entity;

import com.jobPortal.careerBridge.enums.ApplicationStatus;
import com.jobPortal.careerBridge.exception.BusinessRuleException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications",
uniqueConstraints = {
@UniqueConstraint(
        name="uk_application_job_application",
        columnNames = {"job_id","applicant_user_id"}
)
}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "job_id",nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "applicant_user_id",nullable = false)
    private User applicant;

    @Column(name = "resume_file_name",nullable = false)
    private String resumeFileName;

    @Column(name = "resume_storage_key",nullable = false,unique = true,length = 255)
    private String resumeStorageKey;

    @Column(name = "resume_content_type",nullable = false)
    private String resumeContentType;

    @Column(name = "resume_file_size",nullable = false)
    private Long resumeFileSize;

    @Column(name = "cover_letter",columnDefinition = "TEXT")
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    @Column(name = "applied_at",updatable = false)
    private LocalDateTime appliedAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Application(
            Job job,
            User applicant,
            String resumeFileName,
            String resumeStorageKey,
            String resumeContentType,
            Long resumeFileSize,
            String coverLetter
    ){
        this.job = job;
        this.applicant = applicant;
        this.resumeFileName = resumeFileName;
        this.resumeStorageKey = resumeStorageKey;
        this.resumeContentType = resumeContentType;
        this.resumeFileSize = resumeFileSize;
        this.coverLetter = coverLetter;
        this.appliedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

    public void markUnderReview(){
        if(this.status != ApplicationStatus.SUBMITTED){
            throw new BusinessRuleException(
                    "Only submitted applications can move to under review"
            );
        }
        this.status = ApplicationStatus.UNDER_REVIEW;
        this.updatedAt = LocalDateTime.now();
    }

    public void shortList(){
        if(this.status != ApplicationStatus.SUBMITTED &&
                this.status != ApplicationStatus.UNDER_REVIEW){
            throw new BusinessRuleException(
                    "Application cannot be shortlisted from its current status"
            );
        }
        this.status = ApplicationStatus.SHORTLISTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject(){
        if(this.status == ApplicationStatus.REJECTED ||
        this.status == ApplicationStatus.WITHDRAWN){
            throw new BusinessRuleException(
                    "This application cannot be rejected"
            );
        }
        this.status = ApplicationStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void withdraw(){
        if(this.status == ApplicationStatus.REJECTED ||
        this.status == ApplicationStatus.WITHDRAWN){
            throw new BusinessRuleException(
              "This application cannot be withdrawn"
            );
        }
        this.status = ApplicationStatus.WITHDRAWN;
        this.updatedAt = LocalDateTime.now();
    }


}
