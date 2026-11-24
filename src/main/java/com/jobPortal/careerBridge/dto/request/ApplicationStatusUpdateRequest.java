package com.jobPortal.careerBridge.dto.request;

import com.jobPortal.careerBridge.enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplicationStatusUpdateRequest {

    @NotNull(message = "Application status is required")
    private ApplicationStatus status;

}
