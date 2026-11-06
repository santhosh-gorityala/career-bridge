package com.jobPortal.careerBridge.entity;

import com.jobPortal.careerBridge.enums.EmploymentType;
import com.jobPortal.careerBridge.enums.JobStatus;
import com.jobPortal.careerBridge.enums.WorkplaceType;
import com.jobPortal.careerBridge.exception.BusinessRuleException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "company_id",nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "created_by",nullable = false)
    private User createdBy;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type" ,length = 30)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "workplace_type",length = 30)
    private WorkplaceType workplaceType;

    @Column(length = 255)
    private String location;

    @Column(name = "experience_min")
    private Integer experienceMin;

    @Column(name = "experience_max")
    private Integer experienceMax;

    @Column(name = "salary_min",precision = 12,scale = 2)
    private BigDecimal salaryMin;

    @Column(name = "salary_max",precision = 12,scale = 2)
    private BigDecimal salaryMax;

    @Column(nullable = false)
    private Integer vacancies = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private JobStatus status = JobStatus.DRAFT;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;


    public Job(
        Company company,
        User createdBy,
        String title,
        String description,
        EmploymentType employmentType,
        WorkplaceType workplaceType,
        String location,
        Integer experienceMin,
        Integer experienceMax,
        BigDecimal salaryMin,
        BigDecimal salaryMax,
        Integer vacancies,
        LocalDate applicationDeadline,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        this.company = company;
        this.createdBy = createdBy;
        this.title = title;
        this.description = description;
        this.employmentType = employmentType;
        this.workplaceType = workplaceType;
        this.location = location;
        this.experienceMin = experienceMin;
        this.experienceMax = experienceMax;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.vacancies = vacancies;
        this.applicationDeadline = applicationDeadline;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validateRanges();
    }

    public void update(
        String title,
        String description,
        EmploymentType employmentType,
        WorkplaceType workplaceType,
        String location,
        Integer experienceMin,
        Integer experienceMax,
        BigDecimal salaryMin,
        BigDecimal salaryMax,
        Integer vacancies,
        LocalDate applicationDeadline
    ){

        this.title = title;
        this.description = description;
        this.employmentType = employmentType;
        this.workplaceType = workplaceType;
        this.location = location;
        this.experienceMin = experienceMin;
        this.experienceMax = experienceMax;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.vacancies = vacancies;
        this.applicationDeadline = applicationDeadline;
        validateRanges();
        this.updatedAt = LocalDateTime.now();
    }

    private void validateRanges() {

        if (experienceMin != null && experienceMax != null
                && experienceMin > experienceMax) {
            throw new BusinessRuleException(
                    "Minimum experience cannot exceed maximum experience"
            );
        }

        if (salaryMin != null && salaryMax != null
                && salaryMin.compareTo(salaryMax) > 0) {
            throw new BusinessRuleException(
                    "Minimum salary cannot exceed maximum salary"
            );
        }
    }

    public void ensureUpdatable(){
        if(this.status == JobStatus.CLOSED ||
           this.status == JobStatus.EXPIRED){
            throw new BusinessRuleException(
                    "Closed or Expired jobs cannot be updated"
            );
        }
    }

    public void validateForPublishing(){

        if(title == null || title.isBlank()){
            throw new BusinessRuleException(
                    "Job title is required to publish"
            );
        }

        if(description == null || description.isBlank()){
            throw new BusinessRuleException(
                    "Job description is required to publish"
            );
        }

        if(employmentType == null){
            throw new BusinessRuleException(
              "Employment type is required to publish"
            );
        }

        if(workplaceType == null){
            throw new BusinessRuleException(
                    "Workplace type is required to publish"
            );
        }

        if(vacancies == null || vacancies < 1){
            throw new BusinessRuleException(
                    "At least one vacancy is required"
            );
        }

        validateRanges();

        if(applicationDeadline != null
        && !applicationDeadline.isAfter(LocalDate.now())
        ){
            throw new BusinessRuleException(
                    "Application deadline must be in the future"
            );
        }

    }

    public void publish(){
        if(this.status != JobStatus.DRAFT){
            throw new BusinessRuleException(
                    "Only draft jobs can be published"
            );
        }
        validateForPublishing();

        this.status = JobStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void close(){

        if(this.status != JobStatus.PUBLISHED){
            throw new BusinessRuleException(
                    "Only published jobs can be closed"
            );
        }

        this.status = JobStatus.CLOSED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reopen(){
        if(this.status != JobStatus.CLOSED){
            throw new BusinessRuleException(
                    "Only closed jobs can be reopened"
            );
        }
        if(applicationDeadline != null
        && !applicationDeadline.isAfter(LocalDate.now())){
            throw new BusinessRuleException(
                    "Cannot reopen a job with an expired application deadline"
            );
        }
        this.status = JobStatus.PUBLISHED;
        this.updatedAt = LocalDateTime.now();
    }

    public void ensureDeletable(){
        if(this.status != JobStatus.DRAFT){
            throw new BusinessRuleException(
              "Only draft jobs can be deleted"
            );
        }
    }

    public void markExpired(){
        if (this.status != JobStatus.PUBLISHED) {
            throw new BusinessRuleException(
                    "Only published jobs can be marked as expired"
            );
        }

        if (this.applicationDeadline == null
                || this.applicationDeadline.isAfter(LocalDate.now())) {
            throw new BusinessRuleException(
                    "Job cannot be expired before its application deadline"
            );
        }
        this.status = JobStatus.EXPIRED;
        this.updatedAt = LocalDateTime.now();
    }



}
