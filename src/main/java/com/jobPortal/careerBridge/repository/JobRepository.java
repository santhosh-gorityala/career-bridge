package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job,Long> {
    Optional<Job> findByIdAndCreatedById
            (Long jobId,
             Long createdById
            );
    List<Job> findAllByCompanyId(Long companyId);
    List<Job>findAllByCreatedById(Long createdById);

}
