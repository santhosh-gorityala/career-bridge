package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

    // checking whether a candidate has already applied for job
    boolean existsByJobIdAndApplicantId(
            Long jobId,
            Long applicantId
    );

    // Find one application belonging to a particular candidate
    Optional<Application> findByIdAndApplicantId(
            Long applicationId,
            Long applicantId
    );

    // retrieving an candidate's all applications with pagination
    Page<Application> findAllByApplicantId(
            Long applicantId,
            Pageable pageable
    );

    // retrieving all applications submitted for a particular job
    Page<Application> findAllByJobId(
            Long jobId,
            Pageable pageable
    );

}
