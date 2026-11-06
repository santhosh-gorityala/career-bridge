package com.jobPortal.careerBridge.repository;


import com.jobPortal.careerBridge.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {

    List<Company> findAllByOwnerId(Long userId);

    Optional<Company> findByIdAndOwnerId(
            Long companyId,
            Long ownerId
    );

}
