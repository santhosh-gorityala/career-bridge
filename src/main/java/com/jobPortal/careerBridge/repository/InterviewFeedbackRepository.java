package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.entity.InterviewFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewFeedbackRepository extends
        JpaRepository<InterviewFeedback,Long> {

    // Get feedback for one specific interview
    Optional<InterviewFeedback> findByInterviewId(Long interviewId);

    // Check whether feedback already exists for an interview
    boolean existsByInterviewId(Long interviewId);

    // Get all feedback records belonging to an application,
    // newest feedback first, with pagination
    Page<InterviewFeedback>
    findAllByInterviewRecruitmentApplicationIdOrderBySubmittedAtDesc(
            Long applicationId,
            Pageable pageable
    );


}
