package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.request.UserProfileRequest;
import com.jobPortal.careerBridge.dto.response.UserProfileResponse;
import com.jobPortal.careerBridge.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class UserProfileController {

    private UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(){
        UserProfileResponse userProfileResponse =
                userProfileService.getMyProfile();
        return ResponseEntity.status(HttpStatus.OK).body(userProfileResponse);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            @Valid @RequestBody UserProfileRequest userProfileRequest
            ){
        UserProfileResponse response =
                userProfileService.
                        updateMyProfile(userProfileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
