package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.entity.Interview;
import com.jobPortal.careerBridge.enums.InterviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview,Long> {

    Optional<Interview> findByIdAndRecruitmentId(Long id, Long recruitmentId);

    @EntityGraph(attributePaths = {
            "interviewer",
            "recruitment",
            "recruitment.application"
    })
    Page<Interview> findAllByRecruitmentId(Long recruitmentId, Pageable pageable);

    boolean existsByRecruitmentId(Long recruitmentId);

    List<Interview> findAllByRecruitmentIdAndStatusOrderByScheduledAtAsc(
            Long recruitmentId,
            InterviewStatus status
    );

    @EntityGraph(attributePaths = {
            "interviewer",
            "recruitment",
            "recruitment.application"
    })
    Optional<Interview> findWithDetailsById(Long id);


}
