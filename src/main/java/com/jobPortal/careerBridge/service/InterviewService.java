package com.jobPortal.careerBridge.service;


import com.jobPortal.careerBridge.dto.request.RescheduleInterviewRequest;
import com.jobPortal.careerBridge.dto.request.ScheduleInterviewRequest;
import com.jobPortal.careerBridge.dto.response.InterviewResponse;
import com.jobPortal.careerBridge.dto.response.InterviewerSummaryResponse;
import com.jobPortal.careerBridge.dto.response.PageResponse;
import com.jobPortal.careerBridge.entity.*;
import com.jobPortal.careerBridge.enums.*;
import com.jobPortal.careerBridge.exception.ResourceNotFoundException;
import com.jobPortal.careerBridge.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final CurrentUserService currentUserService;
    private final ApplicationRecruitmentRepository recruitmentRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final CompanyMembershipRepository companyMembershipRepository;


    private void validateInterviewScheduling(
            ScheduleInterviewRequest request
    ){
        String meetingLink = request.getMeetingLink();
        String location = request.getLocation();
        InterviewMode interviewMode = request.getMode();
        if(interviewMode == InterviewMode.ONLINE
                && (meetingLink == null || meetingLink.isBlank())){
        throw new IllegalArgumentException(
                "Meeting Link is required for online interviews"
        );
        }
        if(interviewMode == InterviewMode.IN_PERSON &&
                ( location == null || location.isBlank() )){
        throw new IllegalArgumentException(
                "Location is required for in-person interviews"
        );
        }
    }

    @Transactional
    public InterviewResponse scheduleAnInterview(
       Long applicationId,
       ScheduleInterviewRequest request
    ){
        // 1. Get Logged-in recruiter ID
        Long recruiterId = currentUserService.getCurrentUserId();

        // 2. Find the recruitment process
        ApplicationRecruitment recruitment =
                recruitmentRepository.
                findByApplicationId(applicationId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Application not found"
                        )
                );
        // 3. Verify recruiter owns the job
        Long jobId = recruitment.getApplication().getJob().getId();

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

        Long companyId = job.getCompany().getId();

        // 4. Verify recruitment stage
        validateRecruitmentStage(
                recruitment.getCurrentStage(),
                request.getInterviewType()
        );

        // 5. Validate interview mode
        validateInterviewScheduling(request);
        validateTimezone(request.getTimezone());

        // 6. Find recruiter entity
        User recruiter =
                userRepository.
                findById(recruiterId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Recruiter not found"
                        )
                );

        // 7. Validate and find assign interviewer
        User interviewer = validateInterviewer(
                request.getInterviewerId(),
                companyId
        );

        // 8. Create interview
        Interview interview = new Interview(
                recruitment,
                request.getRoundName(),
                request.getInterviewType(),
                convertToUtc(
                        request.getScheduledAt(),
                        request.getTimezone()
                ),
                request.getDurationMinutes(),
                request.getTimezone(),
                request.getMode(),
                request.getMeetingLink(),
                request.getLocation(),
                request.getInstructions(),
                recruiter,
                interviewer
        );

        Interview savedInterview =
                interviewRepository.save(interview);
        return mapToResponse(savedInterview);
    }

    @Transactional(readOnly = true)
    public PageResponse<InterviewResponse> getInterviewsForApplication(
            Long applicationId,
            Pageable pageable
    ){
        validatePagination(pageable);
        Long recruiterId = currentUserService.getCurrentUserId();
        ApplicationRecruitment recruitment =
                recruitmentRepository.
                findByApplicationId(applicationId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Application not found"
                        )
                );
        Long jobId = recruitment.getApplication().getJob().getId();
        jobRepository.findByIdAndCreatedById(
                jobId,
                recruiterId
        ).orElseThrow(()->
                new ResourceNotFoundException(
                        "Application not found"
                )
        );

        Page<Interview> interviewPage =
                interviewRepository.
                        findAllByRecruitmentId(
                                recruitment.getId(),
                                pageable
                        );
       Page<InterviewResponse> interviewResponse =
               interviewPage.map(
               interview ->
                       mapToResponse(interview)
       );

       return PageResponse.from(interviewResponse);

    }

    @Transactional(readOnly = true)
    public InterviewResponse getInterviewById(Long interviewId){
      // Get authenticated user's ID
      Long currentUserId = currentUserService.getCurrentUserId();

      // Finding interviewId from repository
      Interview interview =
              interviewRepository.
              findWithDetailsById(interviewId).
              orElseThrow(()->
                      new ResourceNotFoundException(
                              "Interview not found with id: "+interviewId
                      )
              );
      // Retrieve current user
      User user =
              userRepository.
              findById(currentUserId).
              orElseThrow(()->
                      new ResourceNotFoundException(
                              "User not found"
                      )
              );
      // check whether the user is admin
      boolean admin = isAdmin(user);

      // check whether the user is interviewer
      boolean isInterviewer =
              interview.getInterviewer().getId().equals(currentUserId);
      // check whether the user is candidate
      boolean isCandidate =
              interview.getRecruitment().getApplication().getApplicant().getId().equals(currentUserId);
      // check whether the user owns the job
      Long jobId = interview.getRecruitment().getApplication().getJob().getId();

      boolean isRecruiter =
              jobRepository.findByIdAndCreatedById(jobId,currentUserId).isPresent();
      // Authorization decision
      boolean allowed = admin
              || isInterviewer
              || isCandidate
              || isRecruiter;

      if(!allowed){
          throw new AccessDeniedException("You are not authorized to access this resource");
      }
      // Convert entity to response
      return mapToResponse(interview);
    }

    @Transactional
    public InterviewResponse rescheduleInterview(
            Long interviewId,
            RescheduleInterviewRequest request
    ){
        // Get authenticated user ID
        Long userId = currentUserService.getCurrentUserId();

        // Find interview with required relationship
        Interview interview =
                interviewRepository.
                findWithDetailsById(interviewId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Interview not found with id: "+interviewId
                        )
                );

        // Load current user
        User currentUser =
                userRepository.
                findById(userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        // check whether the user is admin
        boolean admin = isAdmin(currentUser);

        // check authorization: job owner or admin
        Long jobId = interview.getRecruitment().getApplication().getJob().getId();

        boolean isRecruiter =
                jobRepository.
                findByIdAndCreatedById(
                        jobId,
                        userId
                ).isPresent();
        if(!admin && !isRecruiter){
            throw new AccessDeniedException(
                    "You are not authorized to reschedule this interview"
            );
        }

        // validate interview status
        if(interview.getStatus() != InterviewStatus.SCHEDULED){
            throw new IllegalStateException(
                    "Only scheduled interviews can be rescheduled"
            );
        }


        // validate new date/time using the interview's existing timezone
        validateInterviewDateTime(
                request.getScheduledAt(),
                interview.getTimezone()
        );

        // Convert new date/time to UTC
        LocalDateTime scheduledAtUtc = convertToUtc(
                request.getScheduledAt(),
                interview.getTimezone()
        );

        // update interview
        interview.reschedule(
                scheduledAtUtc,
                request.getDurationMinutes()
        );

        // return updated response
        return mapToResponse(interview);
    }

    @Transactional
    public InterviewResponse cancelInterview(Long interviewId){
        // current authenticated user ID
        Long userId = currentUserService.getCurrentUserId();

        // Fetching interview by ID
        Interview interview =
                interviewRepository.
                findWithDetailsById(interviewId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Interview not found with id: "+interviewId
                        )
                );

        User user =
                userRepository.
                        findById(userId).
                        orElseThrow(()->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        boolean isAdmin = isAdmin(user);

        Long jobId = interview.getRecruitment().getApplication().getJob().getId();

        boolean isRecruiter = jobRepository.findByIdAndCreatedById(jobId,userId).isPresent();

        if(!isAdmin && !isRecruiter){
            throw new AccessDeniedException(
                    "You are not authorized to cancel this interview"
            );
        }

        interview.cancel();

        return mapToResponse(interview);
    }

    @Transactional
    public InterviewResponse completeInterview(Long interviewId){
        // Get authenticated user ID
        Long userId = currentUserService.getCurrentUserId();

        // check if user exists
        User user = userRepository.
                findById(userId)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        // checking interview ID
        Interview interview = interviewRepository.
                findWithDetailsById(interviewId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Interview not found with id: "+interviewId
                        )
                );

        // check is Admin
        boolean isAdmin = isAdmin(user);

        // check whether user is recruiter
        Long jobId = interview.getRecruitment().getApplication().getJob().getId();
        boolean isRecruiter = jobRepository.findByIdAndCreatedById(jobId,userId).isPresent();

        // authorization checking
        if(!isAdmin && !isRecruiter){
            throw new AccessDeniedException(
                    "You are not authorized to complete this interview"
            );
        }

        interview.complete();

        return mapToResponse(interview);
    }

    private void validateInterviewDateTime(
            LocalDateTime scheduledAt,
            String timezone
    ){
        ZoneId zoneId = ZoneId.of(timezone);

        boolean isFuture = scheduledAt
                .atZone(zoneId)
                .toInstant()
                .isAfter(java.time.Instant.now());

        if(!isFuture){
            throw new IllegalArgumentException(
                    "Interview must be scheduled for a future date and time"
            );
        }

    }

    private boolean isAdmin(User user){
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));
    }

    private void validatePagination(Pageable pageable){

        if(pageable.getPageNumber() < 0){
            throw new IllegalArgumentException(
                    "Page number cannot be negative"
            );
        }

        if(pageable.getPageSize() < 1 ||
                pageable.getPageSize() > 100){
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        Set<String> allowedSortFields = Set.of(
                "scheduledAt",
                "createdAt",
                "updatedAt",
                "status"
        );

        pageable.getSort().forEach(order -> {
                if(!allowedSortFields.contains(order.getProperty())){
                    throw new IllegalArgumentException(
                            "Invalid sort field: "+order.getProperty()
                    );
                }
                }
        );

    }


    private void validateRecruitmentStage(
            RecruitmentStage currentStage,
            InterviewType interviewType){
        boolean valid = switch (interviewType){
            case TECHNICAL ,CODING,SYSTEM_DESIGN->
                    currentStage == RecruitmentStage.TECHNICAL_INTERVIEW;
            case MANAGERIAL ->
                    currentStage == RecruitmentStage.MANAGER_INTERVIEW;
            case HR ->
                    currentStage == RecruitmentStage.HR_INTERVIEW;
            case BEHAVIORAL,OTHER ->
                    currentStage == RecruitmentStage.TECHNICAL_INTERVIEW
                            || currentStage == RecruitmentStage.MANAGER_INTERVIEW
                            || currentStage == RecruitmentStage.HR_INTERVIEW;
        };
        if(!valid){
            throw new IllegalArgumentException(
                    "Interview type does not match the candidate's current recruitment stage"
            );
        }
    }

    private void validateTimezone(String timezone){
        try{
            ZoneId.of(timezone);
        }catch (DateTimeException ex){
            throw new IllegalArgumentException(
                    "Invalid timezone: "+timezone
            );
        }
    }

    private void validateInterviewDateTime(
            ScheduleInterviewRequest request
    ) {
        ZoneId zoneId = ZoneId.of(request.getTimezone());

        boolean isFuture = request.getScheduledAt()
                .atZone(zoneId)
                .toInstant()
                .isAfter(java.time.Instant.now());

        if (!isFuture) {
            throw new IllegalArgumentException(
                    "Interview must be scheduled for a future date and time"
            );
        }
    }

    private User validateInterviewer(
            Long interviewerId,
            Long companyId
    ){
        // Find interviewer
        User interviewer =
                userRepository.
                findById(interviewerId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Interviewer not found"
                        )
                );
        // check account status
        if(interviewer.getStatus() != UserStatus.ACTIVE){
            throw new IllegalArgumentException(
                    "Interviewer account is not active"
            );
        }
        // check eligible roles
        boolean eligible = interviewer.getRoles()
                .stream()
                .anyMatch(role ->
                        role.getName().equals("RECRUITER")
                        || role.getName().equals("HIRING_MANAGER")
                );

        if(!eligible){
            throw new IllegalArgumentException(
                    "Interviewer must have RECRUITER or HIRING_MANAGER role"
            );
        }

        // check active company membership
        boolean isCompanyMember =
                companyMembershipRepository.
                        existsByCompanyIdAndUserIdAndStatus(
                        companyId,
                        interviewerId,
                        CompanyMembershipStatus.ACTIVE
                );
        if(!isCompanyMember){
            throw new IllegalArgumentException(
                    "Interviewer must be an active member of the job's company"
            );
        }

        return interviewer;
    }

    private LocalDateTime convertToUtc(
            LocalDateTime scheduledAt,
            String timezone
    ) {
        return scheduledAt
                .atZone(ZoneId.of(timezone))
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
    }
    private InterviewResponse mapToResponse(Interview interview){
        Long interviewerId = interview.getInterviewer().getId();
        String interviewerName = interview.getInterviewer().getFirstName()+" "+
                interview.getInterviewer().getLastName();
        return new InterviewResponse(
                interview.getId(),
                interview.getRecruitment().getApplication().getId(),
                interview.getRecruitment().getId(),
                interview.getRoundName(),
                interview.getInterviewType(),
                interview.getScheduledAt(),
                interview.getDurationMinutes(),
                interview.getTimezone(),
                interview.getMode(),
                interview.getMeetingLink(),
                interview.getLocation(),
                interview.getStatus(),
                interview.getInstructions(),
                interview.getCreatedAt(),
                interview.getUpdatedAt(),
                new InterviewerSummaryResponse(
                        interviewerId,
                        interviewerName
                )
        );
    }

}
