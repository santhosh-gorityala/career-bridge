package com.jobPortal.careerBridge.dto.request;

import com.jobPortal.careerBridge.enums.InterviewRecommendation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateInterviewFeedbackRequest {

    @NotNull(message = "Overall rating is required")
    @Min(value = 1,message = "Rating must be at least 1")
    @Max(value = 5,message = "Rating cannot exceed 5")
    private Integer overallRating;

    @NotNull(message = "Technical rating is required")
    @Min(value = 1,message = "Rating must be at least 1")
    @Max(value = 5,message = "Rating cannot exceed 5")
    private Integer technicalRating;

    @NotNull(message = "Communication rating is required")
    @Min(value = 1,message = "Rating must be at least 1")
    @Max(value = 5,message = "Rating cannot exceed 5")
    private Integer communicationRating;

    @NotNull(message = "Problem solving rating is required")
    @Min(value = 1,message = "Rating must be at least 1")
    @Max(value = 5,message = "Rating cannot exceed 5")
    private Integer problemSolvingRating;

    @NotBlank(message = "Strengths are required")
    private String strengths;

    @NotBlank(message = "Areas to improve are required")
    private String areasToImprove;

    private String comments;

    @NotNull(message = "Recommendation is required")
    private InterviewRecommendation recommendation;

}
