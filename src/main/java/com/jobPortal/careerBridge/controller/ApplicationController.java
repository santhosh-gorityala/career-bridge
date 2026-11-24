package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.request.ApplicationStatusUpdateRequest;
import com.jobPortal.careerBridge.dto.request.ApplicationSubmitRequest;
import com.jobPortal.careerBridge.dto.response.ApplicationResponse;
import com.jobPortal.careerBridge.dto.response.PageResponse;
import com.jobPortal.careerBridge.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasAuthority('JOB_APPLY')")
    public ResponseEntity<ApplicationResponse> submitApplication(
            @Valid @RequestBody ApplicationSubmitRequest submitRequest
            ){
        ApplicationResponse applicationResponse =
                applicationService.submitApplication(submitRequest);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(applicationResponse);
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasAuthority('JOB_APPLY')")
    public ResponseEntity<PageResponse<ApplicationResponse>> getMyApplications(
       @RequestParam(defaultValue = "0") int page,
       @RequestParam(defaultValue = "10") int size
    ){
        PageResponse<ApplicationResponse> applicationResponse =
                applicationService.
                getMyApplications(
                        page,
                        size
                );
        return ResponseEntity.
                status(HttpStatus.OK).
                body(applicationResponse);
    }

    @GetMapping("/{applicationId}")
    @PreAuthorize("hasAuthority('JOB_APPLY')")
    public ResponseEntity<ApplicationResponse> getMyApplication(
            @PathVariable Long applicationId
    ){
        ApplicationResponse applicationResponse =
                applicationService.
                getMyApplicationById(applicationId);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(applicationResponse);
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAuthority('APPLICATION_VIEW')")
    public ResponseEntity<PageResponse<ApplicationResponse>> getJobApplications(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        PageResponse<ApplicationResponse> responsePage =
                applicationService.
                getJobApplications(jobId,page,size);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(responsePage);
    }

    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasAuthority('APPLICATION_UPDATE')")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusUpdateRequest request
            ){
        ApplicationResponse applicationResponse =
                applicationService.updateApplicationStatus(applicationId,request);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(applicationResponse);
    }

    @PatchMapping("/{applicationId}/withdraw")
    @PreAuthorize("hasAuthority('JOB_APPLY')")
    public ResponseEntity<ApplicationResponse> withdrawApplication(
        @PathVariable  Long applicationId
    ){
        ApplicationResponse applicationResponse =
                applicationService.withdrawApplication(applicationId);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(applicationResponse);
    }


}
