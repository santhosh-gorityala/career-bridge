package com.jobPortal.careerBridge.entity;

import com.jobPortal.careerBridge.enums.InterviewRecommendation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "interview_feedback",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_interview_feedback_interview",
                        columnNames = "interview_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "interview_id",nullable = false,unique = true)
    private Interview interview;

    @Column(name = "overall_rating",nullable = false)
    private Integer overallRating;

    @Column(name = "technical_rating",nullable = false)
    private Integer technicalRating;

    @Column(name = "communication_rating",nullable = false)
    private Integer communicationRating;

    @Column(name = "problem_solving_rating",nullable = false)
    private Integer problemSolvingRating;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String strengths;

    @Column(name = "areas_to_improve",nullable = false, columnDefinition = "TEXT")
    private String areasToImprove;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private InterviewRecommendation recommendation;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "submitted_by",nullable = false)
    private User submittedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column(name = "submitted_at",nullable = false,updatable = false)
    private LocalDateTime submittedAt;

    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;

    public InterviewFeedback(
       Interview interview,
       Integer overallRating,
       Integer technicalRating,
       Integer communicationRating,
       Integer problemSolvingRating,
       String strengths,
       String areasToImprove,
       String comments,
       InterviewRecommendation recommendation,
       User submittedBy
    ){
        this.interview = interview;
        this.overallRating = overallRating;
        this.technicalRating = technicalRating;
        this.communicationRating = communicationRating;
        this.problemSolvingRating = problemSolvingRating;
        this.strengths = strengths;
        this.areasToImprove = areasToImprove;
        this.comments = comments;
        this.recommendation = recommendation;
        this.submittedBy = submittedBy;
    }

    @PrePersist
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        this.submittedAt = now;
        this.updatedAt = now;
    }

    public void updateFeedback(
            Integer overallRating,
            Integer technicalRating,
            Integer communicationRating,
            Integer problemSolvingRating,
            String strengths,
            String areasToImprove,
            String comments,
            InterviewRecommendation recommendation,
            User updatedBy
    ){
        this.overallRating = overallRating;
        this.technicalRating = technicalRating;
        this.communicationRating = communicationRating;
        this.problemSolvingRating = problemSolvingRating;
        this.strengths = strengths;
        this.areasToImprove = areasToImprove;
        this.comments = comments;
        this.recommendation = recommendation;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }

}
