package com.jobPortal.careerBridge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String headline;
    private String bio;
    private String location;

}
