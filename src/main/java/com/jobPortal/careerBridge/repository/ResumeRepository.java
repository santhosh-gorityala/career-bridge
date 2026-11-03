package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume,Long> {
    Optional<Resume> findByProfileId(Long profileId);
}
