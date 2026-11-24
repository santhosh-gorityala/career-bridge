package com.jobPortal.careerBridge.entity;

import com.jobPortal.careerBridge.enums.RecruitmentStage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "application_stage_history",
        indexes = {
                @Index(
                        name = "idx_recruitment_history",
                        columnList = "recruitment_id,changed_at"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationStageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "recruitment_id",nullable = false)
    private ApplicationRecruitment recruitment;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_stage",length= 40)
    private RecruitmentStage fromStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_stage",nullable = false,length = 40)
    private RecruitmentStage toStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by",nullable = false)
    private User changedBy;

    @Column(length = 1000)
    private String reason;

    @Column(name = "changed_at",nullable = false,updatable = false)
    private LocalDateTime changedAt;

    public void assignRecruitment(ApplicationRecruitment recruitment){
        this.recruitment = recruitment;
    }

    public ApplicationStageHistory(
         ApplicationRecruitment recruitment,
         RecruitmentStage fromStage,
         RecruitmentStage toStage,
         User changedBy,
         String reason
    ){
        this.recruitment = recruitment;
        this.fromStage = fromStage;
        this.toStage = toStage;
        this.changedBy = changedBy;
        this.reason = reason;
    }

    @PrePersist
    protected void onCreate(){
        changedAt = LocalDateTime.now();
    }


}
