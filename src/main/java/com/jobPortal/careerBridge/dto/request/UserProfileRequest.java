package com.jobPortal.careerBridge.dto.request;


import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileRequest {

    @Size(max = 255)
    private String headline;

    @Size(max = 5000)
    private String bio;

    @Size(max=255)
    private String location;

}
