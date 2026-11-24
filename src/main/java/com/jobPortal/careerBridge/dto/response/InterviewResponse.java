package com.jobPortal.careerBridge.dto.response;

import com.jobPortal.careerBridge.enums.InterviewMode;
import com.jobPortal.careerBridge.enums.InterviewStatus;
import com.jobPortal.careerBridge.enums.InterviewType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InterviewResponse {
    private Long id;
    private Long applicationId;
    private Long recruitmentId;
    private String roundName;
    private InterviewType interviewType;
    private LocalDateTime scheduledAt;
    private Integer durationMinutes;
    private String timezone;
    private InterviewMode mode;
    private String meetingLink;
    private String location;
    private InterviewStatus status;
    private String instructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private InterviewerSummaryResponse interviewer;
}
