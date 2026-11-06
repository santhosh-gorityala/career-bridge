package com.jobPortal.careerBridge.dto.request;

import com.jobPortal.careerBridge.enums.EmploymentType;
import com.jobPortal.careerBridge.enums.WorkplaceType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class JobRequest {

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotBlank(message = "Job title is required")
    @Size(max = 255,message = "Job title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Job description is required")
    @Size(max = 10000,message = "Description cannot exceed 10000 characters")
    private String description;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @NotNull(message = "Workplace type is required")
    private WorkplaceType workplaceType;

    @Size(max=255)
    private String location;

    @Min(value = 0,message = "Minimum experience cannot be negative")
    private Integer experienceMin;

    @Min(value = 0,message = "Maximum experience cannot be negative")
    private Integer experienceMax;

    @DecimalMin(value = "0.0",message = "Minimum salary cannot be negative")
    private BigDecimal salaryMin;

    @DecimalMin(value = "0.0",message = "Maximum salary cannot be negative")
    private BigDecimal salaryMax;

    @NotNull(message = "Vacancies are required")
    @Min(value = 1,message = "At least one vacancy is required")
    private Integer vacancies;

    private LocalDate applicationDeadline;


}
