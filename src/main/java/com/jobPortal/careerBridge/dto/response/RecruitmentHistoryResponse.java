package com.jobPortal.careerBridge.dto.response;

import com.jobPortal.careerBridge.enums.RecruitmentStage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecruitmentHistoryResponse {

    private Long historyId;

    private RecruitmentStage fromStage;

    private RecruitmentStage toStage;

    private Long changedBy;

    private String reason;

    private LocalDateTime changedAt;

}
