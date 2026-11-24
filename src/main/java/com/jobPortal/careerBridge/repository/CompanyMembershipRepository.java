package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.entity.CompanyMembership;
import com.jobPortal.careerBridge.enums.CompanyMembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyMembershipRepository
        extends JpaRepository<CompanyMembership,Long> {
    Optional<CompanyMembership> findByCompanyIdAndUserIdAndStatus(
            Long companyId,
            Long userId,
            CompanyMembershipStatus status
    );
    boolean existsByCompanyIdAndUserIdAndStatus(
      Long companyId,
      Long userId,
      CompanyMembershipStatus status
    );

}
