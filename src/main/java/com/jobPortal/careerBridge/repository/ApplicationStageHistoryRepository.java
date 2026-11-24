package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.entity.ApplicationStageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationStageHistoryRepository extends JpaRepository<ApplicationStageHistory,Long> {
    List<ApplicationStageHistory>
    findByRecruitmentIdOrderByChangedAtAsc(
            Long recruitmentId
    );


}

