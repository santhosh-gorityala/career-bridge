package com.jobPortal.careerBridge.service;


import com.jobPortal.careerBridge.dto.request.ApplicationStatusUpdateRequest;
import com.jobPortal.careerBridge.dto.request.ApplicationSubmitRequest;
import com.jobPortal.careerBridge.dto.response.ApplicationResponse;
import com.jobPortal.careerBridge.dto.response.PageResponse;
import com.jobPortal.careerBridge.entity.*;
import com.jobPortal.careerBridge.enums.ApplicationStatus;
import com.jobPortal.careerBridge.enums.JobStatus;
import com.jobPortal.careerBridge.exception.BusinessRuleException;
import com.jobPortal.careerBridge.exception.InvalidSearchParameterException;
import com.jobPortal.careerBridge.exception.ResourceNotFoundException;
import com.jobPortal.careerBridge.repository.*;
import com.jobPortal.careerBridge.service.storage.ApplicationStorageKeyGenerator;
import com.jobPortal.careerBridge.service.storage.FileStorageService;
import com.jobPortal.careerBridge.service.storage.S3FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final ResumeRepository resumeRepository;

    private final CurrentUserService currentUserService;

    private final FileStorageService fileStorageService;

    private final ApplicationStorageKeyGenerator
                applicationStorageKeyGenerator;


    private Job validateAndGetJob(Long jobId){
        Job job = jobRepository.
                findById(jobId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );
        if(job.getStatus() != JobStatus.PUBLISHED){
            throw new BusinessRuleException(
                    "Applications are accepted only for published jobs"
            );
        }
        if(job.getApplicationDeadline()!= null &&
                !job.getApplicationDeadline().isAfter(LocalDate.now())){
            throw new BusinessRuleException(
                    "Application deadline has passed"
            );
        }

        return job;
    }

    private void validateNoDuplicateApplication(
            Long jobId,
            Long applicantId
    ){
        boolean alreadyApplied = applicationRepository.
                existsByJobIdAndApplicantId(jobId,applicantId);
        if(alreadyApplied){
            throw new BusinessRuleException(
                    "Already applied for this job"
            );
        }
    }

    private User getApplicant(Long applicantId){
        return userRepository.
                findById(applicantId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }

    private Resume getApplicantResume(Long applicantId){
        UserProfile userProfile =
                userProfileRepository.
                findByUserId(applicantId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "User profile not found"
                        )
                );

        return resumeRepository.findByProfileId(userProfile.getId()).
                orElseThrow(()->
                        new BusinessRuleException(
                                "Please upload a resume before applying"
                        )
                );
    }

    private String createResumeSnapShot(
            Resume resume,
            Long applicantId
    ){
        String snapshotKey =
                applicationStorageKeyGenerator.generate(
                applicantId,
                resume.getFileName()
        );
        fileStorageService.copy(
                resume.getStorageKey(),
                snapshotKey
        );
        return snapshotKey;
    }

    @Transactional
    public ApplicationResponse submitApplication(ApplicationSubmitRequest request){

        // 1. Get authenticated candidate
        Long applicantId = currentUserService.getCurrentUserId();

        // 2. Validate and retrieve job
        Job job = validateAndGetJob(request.getJobId());

        // 3. Prevent duplicate applications
        validateNoDuplicateApplication(job.getId(),applicantId);

        // 4. Retrieve applicant
        User applicant = getApplicant(applicantId);

        // 5. Retrieve current resume
        Resume resume = getApplicantResume(applicantId);

        // 6.Create application-specific S3 snapshot
        String snapshotKey =
                createResumeSnapShot(resume,applicantId);

        // 7. Create application entity
        Application application = new Application(
          job,
          applicant,
          resume.getFileName(),
          snapshotKey,
          resume.getContentType(),
          resume.getFileSize(),
          request.getCoverLetter()
        );

        try{
            /* saveAndFlush cause we want database errors,
            such as duplicate application violations,
            to occur while we're still handling the save operation.
             */
           Application savedApplication =
                   applicationRepository.saveAndFlush(application);
           return mapToResponse(savedApplication);
        }catch (RuntimeException ex){
            // Database save filed : remove the copied S3 object
            try{
                fileStorageService.delete(snapshotKey);
            }catch (RuntimeException cleanupException){
                ex.addSuppressed(cleanupException);
            }

            throw ex;
        }


    }

    @Transactional(readOnly = true)
    public PageResponse<ApplicationResponse> getMyApplications(
            int page,
            int size
    ){
        Long applicantId = currentUserService.getCurrentUserId();

        if(page < 0){
            throw new InvalidSearchParameterException(
                    "page number cannot be negative"
            );
        }
        if(size < 1 || size > 50){
            throw new InvalidSearchParameterException(
                    "page size must be between 1 and 50"
            );
        }

        Sort sort = Sort.by(
                Sort.Direction.DESC,"appliedAt"
        );

        Pageable pageable = PageRequest.of(page,size,sort);

        Page<Application> applications =
                applicationRepository.
                findAllByApplicantId(
                        applicantId,
                        pageable
                );

        Page<ApplicationResponse> responsePage =
                applications.map(
                application ->
                        mapToResponse(application)
        );

        return PageResponse.from(responsePage);
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getMyApplicationById(
            Long applicationId
    ){
        Long applicantId = currentUserService.getCurrentUserId();

        Application application = applicationRepository.
                findByIdAndApplicantId(
                        applicationId,
                        applicantId
                ).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Application not found"
                        )
                );
        return mapToResponse(application);
    }

    @Transactional(readOnly = true)
    public PageResponse<ApplicationResponse> getJobApplications(
            Long jobId,int page,int size
    ){
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
        if(page < 0){
            throw new InvalidSearchParameterException(
                    "Page number cannot be negative"
            );
        }
        if(size < 1 || size > 50){
            throw new InvalidSearchParameterException(
                    "Page size must be between 1 and 50"
            );
        }

        Sort sort = Sort.by(
                Sort.Direction.DESC,"appliedAt"
        );

        Pageable pageable = PageRequest.of(
                page,size,sort
        );

        Page<Application> applications =
                applicationRepository.findAllByJobId(jobId,pageable);

        Page<ApplicationResponse> responsePage =
                applications.map(
                application ->
                        mapToResponse(application)
        );

        return PageResponse.from(responsePage);
    }

    @Transactional
    public ApplicationResponse updateApplicationStatus(
            Long applicationId,
            ApplicationStatusUpdateRequest request
    ){
        Long recruiterId = currentUserService.getCurrentUserId();

        Application application = applicationRepository.
                findById(applicationId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Application not found"
                        )
                );
        // verify that this recruiter owns the job
        jobRepository.findByIdAndCreatedById(
                application.getJob().getId(),recruiterId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );
        switch (request.getStatus()){
            case UNDER_REVIEW -> application.markUnderReview();
            case SHORTLISTED -> application.shortList();
            case REJECTED -> application.reject();
            default -> throw new BusinessRuleException(
                    "Recruiters cannot set application status to "+
                            request.getStatus()
            );
        }
        return mapToResponse(application);
    }

    @Transactional
    public ApplicationResponse withdrawApplication(
            Long applicationId
    ){
        Long applicantId = currentUserService.getCurrentUserId();
        Application application =
                applicationRepository.
                findByIdAndApplicantId(
                        applicationId,
                        applicantId
                ).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Application not found"
                        )
                );
        application.withdraw();
        return mapToResponse(application);
    }


    private ApplicationResponse mapToResponse(Application application){
        return new ApplicationResponse(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getStatus(),
                application.getResumeFileName(),
                application.getAppliedAt(),
                application.getCoverLetter()
        );
    }



}
