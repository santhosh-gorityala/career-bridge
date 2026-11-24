package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.request.UpdateRecruitmentStageRequest;
import com.jobPortal.careerBridge.dto.response.PageResponse;
import com.jobPortal.careerBridge.dto.response.RecruitmentHistoryResponse;
import com.jobPortal.careerBridge.dto.response.RecruitmentPipelineResponse;
import com.jobPortal.careerBridge.dto.response.RecruitmentResponse;
import com.jobPortal.careerBridge.entity.*;
import com.jobPortal.careerBridge.enums.ApplicationStatus;
import com.jobPortal.careerBridge.enums.RecruitmentStage;
import com.jobPortal.careerBridge.exception.BusinessRuleException;
import com.jobPortal.careerBridge.exception.ResourceNotFoundException;
import com.jobPortal.careerBridge.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationRecruitmentRepository recruitmentRepository;
    private final ApplicationStageHistoryRepository historyRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;


    @Transactional
    public RecruitmentResponse startScreening(Long applicationId){

        // 1. Getting authenticated recruiter ID
        Long recruiterId = currentUserService.getCurrentUserId();

        // 2. Retrieve application
        Application application =
                applicationRepository.
                findById(applicationId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Application not found"
                        )
                );

        // 3. Verify recruiter owns the job
        Long jobId = application.getJob().getId();
        Job job = jobRepository.
                findByIdAndCreatedById(
                        jobId,
                        recruiterId
                ).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );

        // 4. Check whether recruiter has already started
        boolean recruitmentExists =
                recruitmentRepository.
                        findByApplicationId(
                                applicationId
                        ).
                        isPresent();
        if(recruitmentExists){
            throw new BusinessRuleException(
                    "Recruitment has already started for this application"
            );
        }

        // we need User entity cause our ApplicationRecruiter Entity has startedBy field which is of type 'User'
        User recruiter = userRepository.
                findById(recruiterId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Recruiter not found"
                        )
                );

        // create recruitment record
        ApplicationRecruitment recruitment =
                new ApplicationRecruitment(application,recruiter);

        // create initial stage history
        ApplicationStageHistory stageHistory =
                new ApplicationStageHistory(
                        recruitment,
                        null,
                        RecruitmentStage.SCREENING,
                        recruiter,
                        "Screening started"
                );
        // Associate history with recruitment
        recruitment.addStageHistory(stageHistory);

        // update application status
        application.markUnderReview();

        // save recruitment and its history
        ApplicationRecruitment savedRecruitment =
                recruitmentRepository.saveAndFlush(recruitment);

        // Explicitly save initial history record
        historyRepository.saveAndFlush(stageHistory);

        return mapToResponse(savedRecruitment);

    }

    @Transactional(readOnly = true)
    public List<RecruitmentHistoryResponse> getApplicationHistory(Long applicationId){

        // 1. Get Authenticated Recruiter ID
        Long recruiterId = currentUserService.getCurrentUserId();

        // 2. Check application ID
        Application application =
                applicationRepository.
                findById(applicationId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Application not found"
                        )
                );

        // 3. Check whether recruiter owns the job associated with the application
        Long jobId = application.getJob().getId();

        jobRepository.
                findByIdAndCreatedById(
                        jobId,
                        recruiterId
                ).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );

        ApplicationRecruitment recruitment =
                recruitmentRepository.
                findByApplicationId(applicationId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Recruitment record not found for this application"
                        )
                );

        List<ApplicationStageHistory> historyRecords =
                historyRepository.findByRecruitmentIdOrderByChangedAtAsc(recruitment.getId());

        List<RecruitmentHistoryResponse> historyResponse = new ArrayList<>();
        for(ApplicationStageHistory stageHistory : historyRecords){
            historyResponse.add(
                    new RecruitmentHistoryResponse(
                            stageHistory.getId(),
                            stageHistory.getFromStage(),
                            stageHistory.getToStage(),
                            stageHistory.getChangedBy().getId(),
                            stageHistory.getReason(),
                            stageHistory.getChangedAt()
                    )
            );
        }

        return historyResponse;
    }

    private void validateStageTransition(
            RecruitmentStage currentStage,
            RecruitmentStage newStage,
            String reason
    ){
        // prevent changes after a terminal stage
        if(currentStage.isTerminal()){
            throw new BusinessRuleException(
                    "Cannot change recruitment stage after terminal stage"
            );
        }

        // prevent moving to the same stage
        if(currentStage == newStage){
            throw new BusinessRuleException(
                    "Application is already in this recruitment stage"
            );
        }

        // handle rejection
        if(newStage == RecruitmentStage.REJECTED){
            return;
        }

        // prevent direct withdrawal through recruiter stage update
        if(newStage == RecruitmentStage.WITHDRAWN){
            throw new BusinessRuleException(
              "Candidates must withdraw their own applications"
            );
        }

        // hired requires offer acceptance
        if(newStage == RecruitmentStage.HIRED){
            throw new BusinessRuleException(
                    "Candidate can only be marked as hired after offer acceptance"
            );
        }

        if(!currentStage.canMoveForward(newStage)){
            throw new BusinessRuleException(
              "Invalid recruitment stage transition"
            );
        }

        if(currentStage.requiresSkipReason(newStage)
        && (reason == null || reason.isBlank())){
            throw new BusinessRuleException(
                    "A reason is required when skipping recruitment stages"
            );
        }


    }

    @Transactional
    public RecruitmentResponse updateRecruitmentStage(
            Long applicationId,
            UpdateRecruitmentStageRequest request
    ){
        Long recruiterId = currentUserService.getCurrentUserId();

        // check whether application exists
        Application application =
                applicationRepository.findById(applicationId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Application not found"
                        )
                );

        // check whether recruiters job id is associated with application id
        Long jobId = application.getJob().getId();
        jobRepository.
                findByIdAndCreatedById(
                        jobId,
                        recruiterId
                ).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );

        // check whether recruitment record already exists
        ApplicationRecruitment recruitment =
                recruitmentRepository.
                findByApplicationId(applicationId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Recruitment record not found"
                        )
                );

        RecruitmentStage currentStage = recruitment.getCurrentStage();
        RecruitmentStage newStage = request.getNewStage();


        //validate transition before updating stage
        validateStageTransition(
                recruitment.getCurrentStage(),
                newStage,
                request.getReason()
        );

        // synchronize application status
        if(newStage == RecruitmentStage.REJECTED){
            application.reject();
        }else if(application.getStatus() == ApplicationStatus.UNDER_REVIEW){
            application.shortList();
        }

        // Find recruiter
        User recruiter =
                userRepository.
                findById(recruiterId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Recruiter not found"
                        )
                );


        // create history record
        ApplicationStageHistory history = new ApplicationStageHistory(
          recruitment,
          currentStage,
          newStage,
          recruiter,
          request.getReason()
        );



        recruitment.addStageHistory(history);

        recruitment.updateStage(newStage);

        ApplicationRecruitment savedRecruitment =
                recruitmentRepository.save(recruitment);

        historyRepository.saveAndFlush(history);

        return mapToResponse(savedRecruitment);
    }

    @Transactional(readOnly = true)
    public PageResponse<RecruitmentPipelineResponse> getJobRecruitmentPipeline(
            Long jobId,
            Pageable pageable
    ){

        // Retrieve current logged in recruiter ID
        Long recruiterId = currentUserService.getCurrentUserId();

        Job job =
                jobRepository.
                findByIdAndCreatedById(
                        jobId,
                        recruiterId
                ).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );

        Page<ApplicationRecruitment> recruitments=
                recruitmentRepository.findAllByApplicationJobId(
                        jobId,
                        pageable
                );

        Page<RecruitmentPipelineResponse> responsePage =
                recruitments.map(recruitment -> {
            Application application = recruitment.getApplication();
            User applicant = application.getApplicant();
            String candidateName = applicant.getFirstName() + " "+applicant.getLastName();

            return new RecruitmentPipelineResponse(
                    recruitment.getId(),
                    application.getId(),
                    candidateName,
                    applicant.getEmail(),
                    recruitment.getCurrentStage(),
                    recruitment.getStartedAt(),
                    recruitment.getUpdatedAt()
            );
        }
        );

        return  PageResponse.from(responsePage);
    }


    private RecruitmentResponse mapToResponse(
            ApplicationRecruitment recruitment
    ){
        return new RecruitmentResponse(
          recruitment.getId(),
          recruitment.getApplication().getId(),
          recruitment.getCurrentStage(),
          recruitment.getStartedBy().getId(),
          recruitment.getStartedAt()
        );
    }


}
