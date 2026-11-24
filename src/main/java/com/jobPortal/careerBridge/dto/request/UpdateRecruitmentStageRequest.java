package com.jobPortal.careerBridge.dto.request;

import com.jobPortal.careerBridge.enums.RecruitmentStage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateRecruitmentStageRequest {


    @NotNull(message = "New Stage is required")
    private RecruitmentStage newStage;

    @Size(max = 1000,message = "Reason cannot exceed 1000 characters")
    private String reason;
}
