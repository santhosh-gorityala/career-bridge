package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.request.CreateInterviewFeedbackRequest;
import com.jobPortal.careerBridge.dto.request.UpdateInterviewFeedbackRequest;
import com.jobPortal.careerBridge.dto.response.InterviewFeedbackResponse;
import com.jobPortal.careerBridge.service.InterviewFeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
public class InterviewFeedbackController {

    private final InterviewFeedbackService feedbackService;

    @PostMapping("/{interviewId}/feedback")
    @PreAuthorize("hasAuthority('INTERVIEW_FEEDBACK_SUBMIT')")
    public ResponseEntity<InterviewFeedbackResponse> submitFeedback(
            @PathVariable Long interviewId,
            @Valid @RequestBody CreateInterviewFeedbackRequest request
            ){
        InterviewFeedbackResponse feedbackResponse =
                feedbackService.submitFeedback(interviewId,request);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(feedbackResponse);
    }

    @PatchMapping("/{interviewId}/feedback")
    @PreAuthorize("hasAuthority('INTERVIEW_FEEDBACK_SUBMIT')")
    public ResponseEntity<InterviewFeedbackResponse> updateFeedback(
            @PathVariable Long interviewId,
            @Valid @RequestBody UpdateInterviewFeedbackRequest updateRequest
            ){
        InterviewFeedbackResponse feedbackResponse =
                feedbackService.updateFeedback(interviewId,updateRequest);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(feedbackResponse);
    }

    @GetMapping("/{interviewId}/feedback")
    @PreAuthorize("hasAuthority('INTERVIEW_FEEDBACK_SUBMIT')")
    public ResponseEntity<InterviewFeedbackResponse> getFeedback(
            @PathVariable Long interviewId
    ){
        InterviewFeedbackResponse feedbackResponse =
                feedbackService.getFeedback(interviewId);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(feedbackResponse);
    }

}
