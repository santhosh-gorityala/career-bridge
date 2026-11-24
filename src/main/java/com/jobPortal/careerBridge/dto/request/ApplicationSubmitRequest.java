package com.jobPortal.careerBridge.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplicationSubmitRequest {

    @NotNull(message = "Job ID is required")
    @Positive(message = "Job ID must be positive")
    private Long jobId;

    @Size(
            max = 3000,
            message = "Cover letter cannot exceed 3000 characters"
    )
    private String coverLetter;

}
