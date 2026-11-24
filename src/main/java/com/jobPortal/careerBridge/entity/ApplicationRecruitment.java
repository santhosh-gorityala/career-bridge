package com.jobPortal.careerBridge.entity;


import com.jobPortal.careerBridge.enums.RecruitmentStage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "application_recruitment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationRecruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "application_id",nullable = false,unique = true)
    private Application application;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_stage",nullable = false,length = 40)
    private RecruitmentStage currentStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "started_by",nullable = false)
    private User startedBy;

    @Column(name = "started_at",nullable = false,updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @OneToMany(mappedBy = "recruitment")
    private List<ApplicationStageHistory> stageHistory = new ArrayList<>();

    public void updateStage(RecruitmentStage newStage){
        this.currentStage = newStage;
    }

    public void addStageHistory(ApplicationStageHistory history){
        stageHistory.add(history);
        history.assignRecruitment(this);
    }

    public ApplicationRecruitment(
            Application application,
            User startedBy
    ){
        this.application = application;
        this.startedBy = startedBy;
        this.currentStage = RecruitmentStage.SCREENING;
    }

    @PrePersist
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        startedAt = now;
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }


}
