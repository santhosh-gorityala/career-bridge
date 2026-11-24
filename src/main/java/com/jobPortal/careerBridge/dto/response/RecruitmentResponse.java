package com.jobPortal.careerBridge.dto.response;

import com.jobPortal.careerBridge.enums.RecruitmentStage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecruitmentResponse {

    private Long recruitmentId;

    private Long applicationId;

    private RecruitmentStage currentStage;

    private Long startedBy;

    private LocalDateTime startedAt;



}
