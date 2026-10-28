package com.jobPortal.careerBridge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class WorkExperienceResponse {

    private Long id;
    private String companyName;
    private String jobTitle;
    private String employmentType;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrentlyWorking;
    private String description;


}
