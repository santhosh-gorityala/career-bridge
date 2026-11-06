package com.jobPortal.careerBridge.dto.response;

import com.jobPortal.careerBridge.enums.EmploymentType;
import com.jobPortal.careerBridge.enums.JobStatus;
import com.jobPortal.careerBridge.enums.WorkplaceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JobResponse {

    private Long id;
    private Long companyId;
    private String companyName;
    private String title;
    private String description;
    private EmploymentType employmentType;
    private WorkplaceType workplaceType;
    private String location;
    private Integer experienceMin;
    private Integer experienceMax;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private Integer vacancies;
    private JobStatus status;
    private LocalDate applicationDeadline;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
