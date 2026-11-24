package com.jobPortal.careerBridge.dto.response;

import com.jobPortal.careerBridge.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private ApplicationStatus status;
    private String resumeFileName;
    private LocalDateTime appliedAt;
    private String coverLetter;
}
