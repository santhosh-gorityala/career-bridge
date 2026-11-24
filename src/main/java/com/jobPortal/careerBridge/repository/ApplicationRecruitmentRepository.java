package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.dto.response.RecruitmentResponse;
import com.jobPortal.careerBridge.entity.ApplicationRecruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRecruitmentRepository extends JpaRepository<ApplicationRecruitment,Long> {

Optional<ApplicationRecruitment> findByApplicationId(Long applicationId);

Page<ApplicationRecruitment> findAllByApplicationJobId(
        Long jobId,
        Pageable pageable
);

}
