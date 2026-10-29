package com.jobPortal.careerBridge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class EducationResponse {

    private Long id;
    private String degree;
    private String institution;
    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrentlyStudying;
    private String grade;
    private String description;
}
