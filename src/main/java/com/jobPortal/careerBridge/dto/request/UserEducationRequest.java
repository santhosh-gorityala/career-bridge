package com.jobPortal.careerBridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserEducationRequest {

    @NotBlank
    @Size(max = 255)
    private String degree;

    @NotBlank
    @Size(max = 255)
    private String institution;

    @Size(max = 255)
    private String fieldOfStudy;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private boolean currentlyStudying;

    @Size(max = 50)
    private String grade;

    @Size(max = 5000)
    private String description;

}
