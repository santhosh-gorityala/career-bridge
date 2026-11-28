package com.jobPortal.careerBridge.dto.response;

import com.jobPortal.careerBridge.enums.InterviewRecommendation;
import com.jobPortal.careerBridge.enums.InterviewType;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@Builder
public class InterviewFeedbackResponse {

    private Long id;

    private Long interviewId;

    private String roundName;

    private InterviewType interviewType;

    private Integer overallRating;

    private Integer technicalRating;

    private Integer communicationRating;

    private Integer problemSolvingRating;

    private String strengths;

    private String areasToImprove;

    private String comments;

    private InterviewRecommendation recommendation;

    private Long submittedBy;

    private String submittedByName;

    private Long updatedBy;

    private LocalDateTime submittedAt;

    private LocalDateTime updatedAt;


}
