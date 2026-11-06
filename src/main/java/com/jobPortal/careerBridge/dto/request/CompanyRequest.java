package com.jobPortal.careerBridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {

    @NotBlank(message = "Company name is required")
    @Size(min = 2,
          max = 255,
          message = "Company name must be between 2 and 255 characters"
    )
    private String name;

    @Size(
            max = 2000,
            message = "Description cannot exceed 2000 characters"
    )
    private String description;

    @Size(max = 500,message = "Website URL is too long")
    @Pattern(
            regexp = "^(https?://).+",
            message = "Website must start with http:// or https://"
    )
    private String website;

    @Size(max=100,message = "Industry cannot exceed 100 characters")
    private String industry;

    @Pattern(
            regexp = "^(1-10|11-50|51-200|201-500|501-1000|1000\\+)?$",
            message = "Invalid company size"
    )
    private String companySize;

    @Size(max = 255,message = "Location cannot exceed 255 characters")
    private String location;


}
