package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.request.JobRequest;
import com.jobPortal.careerBridge.dto.response.JobResponse;
import com.jobPortal.careerBridge.entity.Company;
import com.jobPortal.careerBridge.entity.Job;
import com.jobPortal.careerBridge.entity.User;
import com.jobPortal.careerBridge.exception.ResourceNotFoundException;
import com.jobPortal.careerBridge.repository.CompanyRepository;
import com.jobPortal.careerBridge.repository.JobRepository;
import com.jobPortal.careerBridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;


    @Autowired
    public JobService(
            JobRepository jobRepository,
            CurrentUserService currentUserService,
            UserRepository userRepository,
            CompanyRepository companyRepository
    ){
        this.jobRepository = jobRepository;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public JobResponse createJob(
            JobRequest jobRequest
    ){
        Long userId = currentUserService.getCurrentUserId();
        User recruiter  =
                userRepository.findById(userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        Company company =
                companyRepository.findByIdAndOwnerId(
                jobRequest.getCompanyId(),
                userId
        ).orElseThrow(()->
                new ResourceNotFoundException(
                        "Company not found"
                )
        );

        LocalDateTime now = LocalDateTime.now();
        Job job = new Job(
                company,
                recruiter,
                jobRequest.getTitle(),
                jobRequest.getDescription(),
                jobRequest.getEmploymentType(),
                jobRequest.getWorkplaceType(),
                jobRequest.getLocation(),
                jobRequest.getExperienceMin(),
                jobRequest.getExperienceMax(),
                jobRequest.getSalaryMin(),
                jobRequest.getSalaryMax(),
                jobRequest.getVacancies(),
                jobRequest.getApplicationDeadline(),
                now,
                now
        );


        Job savedJob = jobRepository.save(job);
        return mapToResponse(savedJob);
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getMyJobs(){
        Long userId = currentUserService.getCurrentUserId();
        User recruiter = userRepository.findById(userId).
                orElseThrow(()->
                new ResourceNotFoundException(
                        "User not found"
                )
                );
        List<Job> jobsList =
                jobRepository.findAllByCreatedById(userId);
        List<JobResponse> jobResponseList = new ArrayList<>();
        for(Job job : jobsList){
            jobResponseList.add(
                    mapToResponse(job)
            );
        }
        return jobResponseList;
    }

    @Transactional(readOnly = true)
    public JobResponse getJobById(
            Long jobId
    ){
        Long userId = currentUserService.getCurrentUserId();
        Job job = jobRepository.findByIdAndCreatedById(
                jobId,
                userId
        ).orElseThrow(()->
                new ResourceNotFoundException(
                        "Job not found"
                )
        );
        return mapToResponse(job);
    }

    @Transactional
    public JobResponse updateMyJob(
            Long jobId,
            JobRequest jobRequest
    ){
        Long userId = currentUserService.getCurrentUserId();
        Job savedJob =
                jobRepository.
                findByIdAndCreatedById(
                        jobId,
                        userId
                ).orElseThrow(()->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );

        savedJob.ensureUpdatable();

        savedJob.update(
               jobRequest.getTitle(),
               jobRequest.getDescription(),
               jobRequest.getEmploymentType(),
               jobRequest.getWorkplaceType(),
               jobRequest.getLocation(),
               jobRequest.getExperienceMin(),
                jobRequest.getExperienceMax(),
                jobRequest.getSalaryMin(),
                jobRequest.getSalaryMax(),
                jobRequest.getVacancies(),
                jobRequest.getApplicationDeadline()
        );
        return mapToResponse(savedJob);
    }

    @Transactional
    public JobResponse publishMyJob(
            Long jobId
    ){
        Long userId = currentUserService.getCurrentUserId();
        Job savedJob =
                jobRepository.
                findByIdAndCreatedById(
                        jobId,
                        userId
                ).orElseThrow(()->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );
        savedJob.publish();
        return mapToResponse(savedJob);
    }

    @Transactional
    public JobResponse closeJob(
            Long jobId
    ){
        Long userId = currentUserService.getCurrentUserId();
        Job savedJob =
                jobRepository.
                findByIdAndCreatedById(
                        jobId,
                        userId
                ).orElseThrow(()->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );
        savedJob.close();
        return mapToResponse(savedJob);
    }

    @Transactional
    public JobResponse reopenJob(
            Long jobId
    ){
        Long userId = currentUserService.getCurrentUserId();
        Job savedJob =
                jobRepository.
                findByIdAndCreatedById(
                        jobId,
                        userId
                ).orElseThrow(()->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );
        savedJob.reopen();
        return mapToResponse(savedJob);
    }

    @Transactional
    public void deleteDraftJob(Long jobId){
        Long userId = currentUserService.getCurrentUserId();
        Job savedJob =
                jobRepository.
                findByIdAndCreatedById(
                        jobId,
                        userId
                ).orElseThrow(()->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );
        savedJob.ensureDeletable();
        jobRepository.delete(savedJob);
    }

    private JobResponse mapToResponse(Job savedJob){
        return new JobResponse(
                savedJob.getId(),
                savedJob.getCompany().getId(),
                savedJob.getCompany().getName(),
                savedJob.getTitle(),
                savedJob.getDescription(),
                savedJob.getEmploymentType(),
                savedJob.getWorkplaceType(),
                savedJob.getLocation(),
                savedJob.getExperienceMin(),
                savedJob.getExperienceMax(),
                savedJob.getSalaryMin(),
                savedJob.getSalaryMax(),
                savedJob.getVacancies(),
                savedJob.getStatus(),
                savedJob.getApplicationDeadline(),
                savedJob.getPublishedAt(),
                savedJob.getCreatedAt(),
                savedJob.getUpdatedAt()
        );
    }


}
