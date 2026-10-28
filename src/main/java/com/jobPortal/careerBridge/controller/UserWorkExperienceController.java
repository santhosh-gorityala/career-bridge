package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.request.UserWorkExperienceRequest;
import com.jobPortal.careerBridge.dto.response.WorkExperienceResponse;
import com.jobPortal.careerBridge.service.WorkExperienceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile/experience")
public class UserWorkExperienceController {
    private final WorkExperienceService workExperienceService;

    @Autowired
    public UserWorkExperienceController(
            WorkExperienceService workExperienceService
    ){
        this.workExperienceService = workExperienceService;
    }

    @PostMapping
    public ResponseEntity<WorkExperienceResponse> addExperience(
            @Valid @RequestBody UserWorkExperienceRequest request
            ){
        WorkExperienceResponse experienceResponse =
                workExperienceService.addExperience(request);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(experienceResponse);
    }

    @GetMapping
    public ResponseEntity<List<WorkExperienceResponse>>
    getMyExperiences(){
        List<WorkExperienceResponse> experienceResponseList =
                workExperienceService.getMyExperience();
        return ResponseEntity.
                status(HttpStatus.OK).
                body(experienceResponseList);
    }

    @PutMapping("/{experienceId}")
    public ResponseEntity<WorkExperienceResponse>
    updateMyExperience(
            @Valid @RequestBody UserWorkExperienceRequest request,
            @PathVariable Long experienceId
    ){
        WorkExperienceResponse experienceResponse =
                workExperienceService.
                updateMyExperience(
                        experienceId,
                        request
                );
        return ResponseEntity.
                status(HttpStatus.OK).
                body(experienceResponse);
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<Void> deleteMyExperience(
    @PathVariable Long experienceId
    ){
        workExperienceService.deleteMyExperience(experienceId);
        return ResponseEntity.
                status(HttpStatus.NO_CONTENT).
                build();
    }





}
