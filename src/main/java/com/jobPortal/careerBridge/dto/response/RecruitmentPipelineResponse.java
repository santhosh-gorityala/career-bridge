package com.jobPortal.careerBridge.dto.response;

import com.jobPortal.careerBridge.enums.RecruitmentStage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecruitmentPipelineResponse {

    private Long recruitmentId;
    private Long applicationId;
    private String candidateName;
    private String candidateEmail;
    private RecruitmentStage currentStage;
    private LocalDateTime startedAt;
    private LocalDateTime updatedAt;

}
