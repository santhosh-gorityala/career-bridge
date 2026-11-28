package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.request.CreateInterviewFeedbackRequest;
import com.jobPortal.careerBridge.dto.request.UpdateInterviewFeedbackRequest;
import com.jobPortal.careerBridge.dto.response.InterviewFeedbackResponse;
import com.jobPortal.careerBridge.entity.Interview;
import com.jobPortal.careerBridge.entity.InterviewFeedback;
import com.jobPortal.careerBridge.entity.User;
import com.jobPortal.careerBridge.enums.InterviewStatus;
import com.jobPortal.careerBridge.exception.FeedbackAlreadySubmittedException;
import com.jobPortal.careerBridge.exception.ResourceNotFoundException;
import com.jobPortal.careerBridge.repository.InterviewFeedbackRepository;
import com.jobPortal.careerBridge.repository.InterviewRepository;
import com.jobPortal.careerBridge.repository.JobRepository;
import com.jobPortal.careerBridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;


@Service
@RequiredArgsConstructor
public class InterviewFeedbackService {

    private final InterviewFeedbackRepository feedbackRepository;
    private final InterviewRepository interviewRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;


    @Transactional
    public InterviewFeedbackResponse submitFeedback(
            Long interviewId,
            CreateInterviewFeedbackRequest request
    ){
        // Get authenticated user id
        Long userId = currentUserService.getCurrentUserId();

        // Find the interview
        Interview interview =
                interviewRepository.
                findWithDetailsById(interviewId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Interview not found with id: "+interviewId
                        )
                );

        // Interview must be completed
        if(interview.getStatus() != InterviewStatus.COMPLETED){
            throw new IllegalStateException(
                    "Feedback can only be submitted for completed interviews"
            );
        }

        // Check whether feedback already exists
        if(feedbackRepository.existsByInterviewId(interviewId)){
            throw new FeedbackAlreadySubmittedException(
                    "Feedback has already been submitted for this interview"
            );
        }

        // Check authorization
        User currentUser = userRepository.
                findById(userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Interviewer not found"
                        )
                );

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));


        boolean isAssignedInterviewer =
                interview.getInterviewer().getId().equals(userId);

        if(!isAssignedInterviewer){
            throw new AccessDeniedException(
                    "Only the assigned interviewer can submit feedback"
            );
        }

        // Create feedback
        InterviewFeedback feedback = new InterviewFeedback(
                interview,
                request.getOverallRating(),
                request.getTechnicalRating(),
                request.getCommunicationRating(),
                request.getProblemSolvingRating(),
                request.getStrengths(),
                request.getAreasToImprove(),
                request.getComments(),
                request.getRecommendation(),
                currentUser
        );

        InterviewFeedback savedFeedback = feedbackRepository.save(feedback);

        return mapToResponse(savedFeedback);
    }

    @Transactional
    public InterviewFeedbackResponse updateFeedback(
            Long interviewId,
            UpdateInterviewFeedbackRequest request
    ){

        // Get authenticated user ID
        Long currentUserId = currentUserService.getCurrentUserId();

        // Find existing feedback
        InterviewFeedback interviewFeedback =
                feedbackRepository.
                        findByInterviewId(interviewId).
                        orElseThrow(()->
                                new ResourceNotFoundException(
                                        "Feedback not found for interview id: "+interviewId
                                )
                        );

        // Find the current user
        User currentUser = userRepository.
                findById(currentUserId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        // check whether current user is admin
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        // check whether current user submitted the feedback
        boolean isAssignedInterviewer =
                interviewFeedback.getSubmittedBy().getId().equals(currentUserId);

        if(!isAdmin && !isAssignedInterviewer){
            throw new AccessDeniedException(
              "Only the original interviewer or admin can update feedback"
            );
        }


        interviewFeedback.updateFeedback(
                request.getOverallRating(),
                request.getTechnicalRating(),
                request.getCommunicationRating(),
                request.getProblemSolvingRating(),
                request.getStrengths(),
                request.getAreasToImprove(),
                request.getComments(),
                request.getRecommendation(),
                currentUser
        );

        return mapToResponse(interviewFeedback);
    }

    @Transactional(readOnly = true)
    public InterviewFeedbackResponse getFeedback(
            Long interviewId
    ){
        // Check authenticated user ID
        Long currentUserId = currentUserService.getCurrentUserId();

        // Check Interview exists
        InterviewFeedback interviewFeedback =
                feedbackRepository.
                findByInterviewId(interviewId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Interview feedback not found with interview id: "+interviewId
                        )
                );

        // Get interview
        Interview interview = interviewFeedback.getInterview();

        // Check assigned interviewer
        boolean isAssignedInterviewer =
                interview.getInterviewer().getId().equals(currentUserId);

        // Check Job owner
        Long jobId = interview.getRecruitment().getApplication().getJob().getId();

        boolean isJobOwner = jobRepository.
                findByIdAndCreatedById(
                        jobId,
                        currentUserId
                ).isPresent();

        if(!isAssignedInterviewer && !isJobOwner){
            throw new AccessDeniedException(
                    "You are not allowed to view this interview feedback"
            );
        }

        return mapToResponse(interviewFeedback);
    }

    private InterviewFeedbackResponse mapToResponse(InterviewFeedback savedFeedback){
        Interview interview = savedFeedback.getInterview();
        return InterviewFeedbackResponse.builder().
                id(savedFeedback.getId()).
                interviewId(interview.getId()).
                roundName(interview.getRoundName()).
                interviewType(interview.getInterviewType()).
                overallRating(savedFeedback.getOverallRating()).
                technicalRating(savedFeedback.getTechnicalRating()).
                communicationRating(savedFeedback.getCommunicationRating()).
                problemSolvingRating(savedFeedback.getProblemSolvingRating()).
                strengths(savedFeedback.getStrengths()).
                areasToImprove(savedFeedback.getAreasToImprove()).
                comments(savedFeedback.getComments()).
                recommendation(savedFeedback.getRecommendation()).
                submittedBy(savedFeedback.getSubmittedBy().getId()).
                submittedByName(savedFeedback.getSubmittedBy().getFirstName()+" "+savedFeedback.getSubmittedBy().getLastName()).
                updatedBy(
                        savedFeedback.getUpdatedBy() == null
                                ? null
                                : savedFeedback.getUpdatedBy().getId()
                ).
                submittedAt(savedFeedback.getSubmittedAt()).
                updatedAt(savedFeedback.getUpdatedAt())
                .build();
    }

}
