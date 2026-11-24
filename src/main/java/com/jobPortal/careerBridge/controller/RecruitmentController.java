package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.request.UpdateRecruitmentStageRequest;
import com.jobPortal.careerBridge.dto.response.PageResponse;
import com.jobPortal.careerBridge.dto.response.RecruitmentHistoryResponse;
import com.jobPortal.careerBridge.dto.response.RecruitmentPipelineResponse;
import com.jobPortal.careerBridge.dto.response.RecruitmentResponse;
import com.jobPortal.careerBridge.service.RecruitmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {
    private final RecruitmentService recruitmentService;

    @PostMapping("/applications/{applicationId}/start-screening")
    @PreAuthorize("hasAuthority('RECRUITMENT_MANAGE')")
    public ResponseEntity<RecruitmentResponse> startScreening(
            @PathVariable Long applicationId
    ){
    RecruitmentResponse recruitmentResponse =
            recruitmentService.startScreening(applicationId);
    return ResponseEntity.
            status(HttpStatus.CREATED).
            body(recruitmentResponse);
    }

    @GetMapping("/applications/{applicationId}/history")
    @PreAuthorize("hasAuthority('RECRUITMENT_MANAGE')")
    public ResponseEntity<List<RecruitmentHistoryResponse>> getApplicationHistory(
            @PathVariable Long applicationId
    ){
      List<RecruitmentHistoryResponse> historyResponses =
              recruitmentService.getApplicationHistory(applicationId);
      return ResponseEntity.
              status(HttpStatus.OK).
              body(historyResponses);
    }

    @PatchMapping("/applications/{applicationId}/stage")
    @PreAuthorize("hasAuthority('RECRUITMENT_MANAGE')")
    public ResponseEntity<RecruitmentResponse> updateRecruitmentStage(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateRecruitmentStageRequest request
            ){
        RecruitmentResponse response =
                recruitmentService.updateRecruitmentStage(
                applicationId,
                request
        );
        return ResponseEntity.
                status(HttpStatus.OK).
                body(response);
    }

    @GetMapping("/jobs/{jobId}/pipeline")
    @PreAuthorize("hasAuthority('RECRUITMENT_MANAGE')")
    public ResponseEntity<PageResponse<RecruitmentPipelineResponse>> getJobRecruitmentPipeline(
            @PathVariable Long jobId,
            @PageableDefault(
                    size = 10,
                    sort = "startedAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ){
        PageResponse<RecruitmentPipelineResponse> response =
                recruitmentService.getJobRecruitmentPipeline(
                        jobId,
                        pageable
                );
        return ResponseEntity.
                status(HttpStatus.OK).
                body(response);

    }


}
