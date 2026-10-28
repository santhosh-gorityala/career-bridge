package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.entity.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience,Long> {
    List<WorkExperience> findByProfileId(Long profileId);
    Optional<WorkExperience> findByIdAndProfileId(Long id,Long profileId);
}
