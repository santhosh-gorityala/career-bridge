package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.request.JobRequest;
import com.jobPortal.careerBridge.dto.request.JobSearchRequest;
import com.jobPortal.careerBridge.dto.response.JobResponse;
import com.jobPortal.careerBridge.dto.response.PageResponse;
import com.jobPortal.careerBridge.entity.Job;
import com.jobPortal.careerBridge.service.JobSearchService;
import com.jobPortal.careerBridge.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;
    private final JobSearchService jobSearchService;

    @Autowired
    public JobController(JobService jobService,
                         JobSearchService jobSearchService){
        this.jobService = jobService;
        this.jobSearchService = jobSearchService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('JOB_CREATE')")
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody JobRequest jobRequest
            ){
        JobResponse jobResponse =
                jobService.createJob(jobRequest);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(jobResponse);
    }

    @GetMapping("/my-jobs")
    @PreAuthorize("hasAuthority('JOB_VIEW')")
    public ResponseEntity<List<JobResponse>> getMyJobs(){
        List<JobResponse> jobResponseList =
                jobService.getMyJobs();
        return ResponseEntity.
                status(HttpStatus.OK).
                body(jobResponseList);
    }

    @GetMapping("/{jobId}")
    @PreAuthorize("hasAuthority('JOB_VIEW')")
    public ResponseEntity<JobResponse> getMyJob(
            @PathVariable Long jobId
    ){
        JobResponse jobResponse =
                jobService.getJobById(jobId);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(jobResponse);
    }

    @PutMapping("/{jobId}")
    @PreAuthorize("hasAuthority('JOB_UPDATE')")
    public ResponseEntity<JobResponse> updateMyJob(
            @PathVariable Long jobId,
            @RequestBody JobRequest jobRequest
    ){
        JobResponse jobResponse =
                jobService.updateMyJob(jobId,jobRequest);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(jobResponse);
    }

    @PatchMapping("/{jobId}/publish")
    @PreAuthorize("hasAuthority('JOB_UPDATE')")
    public ResponseEntity<JobResponse> publishJob(
            @PathVariable Long jobId
    ){
        JobResponse jobResponse =
                jobService.publishMyJob(jobId);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(jobResponse);
    }

    @PatchMapping("/{jobId}/close")
    @PreAuthorize("hasAuthority('JOB_UPDATE')")
    public ResponseEntity<JobResponse> closeJob(
            @PathVariable Long jobId
    ){
        JobResponse jobResponse =
                jobService.closeJob(jobId);
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(jobResponse);
    }

    @PatchMapping("/{jobId}/reopen")
    @PreAuthorize("hasAuthority('JOB_UPDATE')")
    public ResponseEntity<JobResponse> reopenJob(
            @PathVariable Long jobId
    ){
        JobResponse jobResponse =
                jobService.reopenJob(jobId);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(jobResponse);
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasAuthority('JOB_DELETE')")
    public ResponseEntity<Void> deleteDraftJob(
            @PathVariable Long jobId
    ){
        jobService.deleteDraftJob(jobId);
        return ResponseEntity.
                status(HttpStatus.NO_CONTENT).
                build();
    }


    @GetMapping
    @PreAuthorize("hasAuthority('JOB_VIEW')")
    public ResponseEntity<PageResponse<JobResponse>> searchJobs(
            @Valid @ModelAttribute JobSearchRequest jobSearchRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
            ){
        PageResponse<JobResponse> pageJobResponse =
                jobSearchService.searchJobs(jobSearchRequest,page,size,sortBy,direction);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(pageJobResponse);
    }


}
