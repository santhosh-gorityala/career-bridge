package com.jobPortal.careerBridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserWorkExperienceRequest {

    @NotBlank
    @Size(max = 255)
    private String companyName;

    @NotBlank
    @Size(max = 255)
    private String jobTitle;

    @Size(max = 50)
    private String employmentType;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private boolean currentlyWorking;

    @Size(max = 5000)
    private String description;
}
