package com.jobPortal.careerBridge.dto.request;

import com.jobPortal.careerBridge.enums.EmploymentType;
import com.jobPortal.careerBridge.enums.WorkplaceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class JobSearchRequest {

    @Size(max = 100,message = "Keyword cannot exceed 100 characters")
    private String keyword;

    @Size(max = 100,message = "Location cannot exceed 100 characters")
    private String location;


    private EmploymentType employmentType;


    private WorkplaceType workplaceType;

    @Min(value = 0,message = "Experience cannot be negative")
    @Max(value = 50,message = "Experience cannot exceed 50 years")
    private Integer experience;

    @DecimalMin(value = "0.0",message = "Minimum salary cannot be negative")
    private BigDecimal minimumSalary;

    @DecimalMin(value = "0.0",message = "Maximum salary cannot be negative")
    private BigDecimal maximumSalary;


}
