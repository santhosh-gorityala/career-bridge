package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.request.UserEducationRequest;
import com.jobPortal.careerBridge.dto.response.EducationResponse;
import com.jobPortal.careerBridge.service.EducationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile/education")
public class UserProfileEducationController {

    private final EducationService educationService;

    @Autowired
    public UserProfileEducationController(
            EducationService educationService
    ){
        this.educationService = educationService;
    }

    @PostMapping
    public ResponseEntity<EducationResponse> addEducation(
            @Valid @RequestBody UserEducationRequest request
            ){
        EducationResponse educationResponse =
                educationService.addEducation(request);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(educationResponse);
    }

    @GetMapping
    public ResponseEntity<List<EducationResponse>> getMyEducations(){
        List<EducationResponse> educationResponses =
                educationService.getMyEducation();
        return ResponseEntity.
                status(HttpStatus.OK).
                body(educationResponses);
    }

    @PutMapping("/{educationId}")
    public ResponseEntity<EducationResponse> updateMyEducation(
            @PathVariable  Long educationId,
            @Valid @RequestBody UserEducationRequest request
    ){
        EducationResponse educationResponse =
                educationService.updateMyEducation(
                educationId,
                request
        );
        return ResponseEntity.
                status(HttpStatus.OK).
                body(educationResponse);
    }

    @DeleteMapping("/{educationId}")
    public ResponseEntity<Void> deleteMyEducation(
            @PathVariable Long educationId
    ){
        educationService.deleteMyEducation(educationId);
        return ResponseEntity.
                status(HttpStatus.NO_CONTENT)
                .build();
    }

}
