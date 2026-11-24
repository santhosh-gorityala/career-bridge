package com.jobPortal.careerBridge.entity;

import com.jobPortal.careerBridge.enums.InterviewMode;
import com.jobPortal.careerBridge.enums.InterviewStatus;
import com.jobPortal.careerBridge.enums.InterviewType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "interview",
        indexes = {
        @Index(name = "idx_interview_recruitment",columnList = "recruitment_id"),
        @Index(name = "idx_interview_scheduled_at",columnList = "scheduled_at"),
        @Index(name = "idx_interview_status",columnList = "status")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "recruitment_id",nullable = false)
    private ApplicationRecruitment recruitment;

    @Column(name = "round_name",nullable = false, length = 100)
    private String roundName;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_type",nullable = false,length = 40)
    private InterviewType interviewType;

    @Column(name = "scheduled_at",nullable = false)
    private LocalDateTime scheduledAt;

    @Column(name = "duration_minutes",nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false,length = 50)
    private String timezone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private InterviewMode mode;

    @Column(name = "meeting_link",length = 1000)
    private String meetingLink;

    @Column(length = 500)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private InterviewStatus status;

    @Column(length = 2000)
    private String instructions;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "created_by",nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "interviewer_id",nullable = false)
    private User interviewer;

    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    public Interview(
          ApplicationRecruitment recruitment,
          String roundName,
          InterviewType interviewType,
          LocalDateTime scheduledAt,
          Integer durationMinutes,
          String timezone,
          InterviewMode mode,
          String meetingLink,
          String location,
          String instructions,
          User createdBy,
          User interviewer
    ){
        this.recruitment = recruitment;
        this.roundName = roundName;
        this.interviewType = interviewType;
        this.scheduledAt = scheduledAt;
        this.durationMinutes = durationMinutes;
        this.timezone = timezone;
        this.mode = mode;
        this.meetingLink = meetingLink;
        this.location = location;
        this.instructions = instructions;
        this.createdBy = createdBy;
        this.interviewer = interviewer;
        this.status = InterviewStatus.SCHEDULED;
    }

    public void reschedule(
            LocalDateTime newScheduledAt,
            Integer newDurationMinutes
    ){
        if(this.status != InterviewStatus.SCHEDULED){
            throw new IllegalStateException(
                    "Only scheduled interviews can be rescheduled"
            );
        }
        this.scheduledAt = newScheduledAt;
        this.durationMinutes = newDurationMinutes;
    }

    public void cancel(){
        if(this.status != InterviewStatus.SCHEDULED){
            throw new IllegalStateException(
                    "Only scheduled interviews can be cancelled"
            );
        }
        this.status = InterviewStatus.CANCELLED;
    }

    public void complete(){
        if(this.status != InterviewStatus.SCHEDULED){
            throw new IllegalStateException(
                    "Only scheduled interviews can be completed"
            );
        }
        this.status = InterviewStatus.COMPLETED;
    }


}
