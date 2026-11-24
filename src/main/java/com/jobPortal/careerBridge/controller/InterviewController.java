package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.request.RescheduleInterviewRequest;
import com.jobPortal.careerBridge.dto.request.ScheduleInterviewRequest;
import com.jobPortal.careerBridge.dto.response.InterviewResponse;
import com.jobPortal.careerBridge.dto.response.PageResponse;
import com.jobPortal.careerBridge.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;


    @PostMapping("/applications/{applicationId}")
    @PreAuthorize("hasAuthority('INTERVIEW_CREATE')")
    public ResponseEntity<InterviewResponse> scheduleInterview(
            @PathVariable Long applicationId,
            @Valid @RequestBody ScheduleInterviewRequest request
            ){
        InterviewResponse response =
                interviewService.
                        scheduleAnInterview(
                                applicationId,
                                request
                        );
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(response);
    }

    @GetMapping("/applications/{applicationId}")
    @PreAuthorize("hasAuthority('INTERVIEW_VIEW')")
    public ResponseEntity<PageResponse<InterviewResponse>>
    getInterviewsForApplication(
            @PathVariable Long applicationId,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "scheduledAt",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ){
        PageResponse<InterviewResponse> response =
                 interviewService.
                         getInterviewsForApplication(
                                 applicationId,
                                 pageable
                         );
        return ResponseEntity.
                status(HttpStatus.OK).
                body(response);
    }

    @GetMapping("/{interviewId}")
    @PreAuthorize("hasAnyAuthority('INTERVIEW_VIEW','INTERVIEW_CANDIDATE_VIEW')")
    public ResponseEntity<InterviewResponse> getInterviewById(
            @PathVariable Long interviewId
    ){
        InterviewResponse interviewResponse =
                interviewService.getInterviewById(interviewId);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(interviewResponse);
    }

    @PatchMapping("/{interviewId}/reschedule")
    @PreAuthorize("hasAuthority('INTERVIEW_UPDATE')")
    public ResponseEntity<InterviewResponse> rescheduleInterview(
            @PathVariable Long interviewId,
            @Valid @RequestBody RescheduleInterviewRequest request
    ){
        InterviewResponse interviewResponse =
                interviewService.rescheduleInterview(interviewId,request);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(interviewResponse);
    }

    @PatchMapping("/{interviewId}/cancel")
    @PreAuthorize("hasAuthority('INTERVIEW_UPDATE')")
    public ResponseEntity<InterviewResponse> cancelInterview(
            @PathVariable Long interviewId
    ){
        InterviewResponse interviewResponse =
                interviewService.cancelInterview(interviewId);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(interviewResponse);
    }

    @PatchMapping("/{interviewId}/complete")
    @PreAuthorize("hasAuthority('INTERVIEW_UPDATE')")
    public ResponseEntity<InterviewResponse> completeInterview(
            @PathVariable Long interviewId
    ){
        InterviewResponse interviewResponse =
                interviewService.completeInterview(interviewId);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(interviewResponse);
    }

}
