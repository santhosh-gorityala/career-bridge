package com.jobPortal.careerBridge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CompanyResponse {

    private Long id;
    private String name;
    private String description;
    private String website;
    private String industry;
    private String companySize;
    private String location;
    private boolean logoAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
