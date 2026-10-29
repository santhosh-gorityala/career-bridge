package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducationRepository extends JpaRepository<Education,Long> {
    List<Education> findByProfileId(Long profileId);

    Optional<Education> findByIdAndProfileId(Long id, Long profileId);

}
