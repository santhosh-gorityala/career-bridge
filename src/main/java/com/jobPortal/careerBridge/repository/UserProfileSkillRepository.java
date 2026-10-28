package com.jobPortal.careerBridge.repository;


import com.jobPortal.careerBridge.entity.UserProfileSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProfileSkillRepository extends JpaRepository<UserProfileSkill,Long> {
    List<UserProfileSkill> findByProfileId(Long profileId);
    boolean existsByProfileIdAndSkillId(
            Long profileId,
            Long skillId
    );
    void deleteByProfileIdAndSkillId(
            Long profileId,
            Long skillId
    );
}
