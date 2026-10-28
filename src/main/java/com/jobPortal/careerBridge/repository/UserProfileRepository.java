package com.jobPortal.careerBridge.repository;

import com.jobPortal.careerBridge.entity.User;
import com.jobPortal.careerBridge.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

    Optional<UserProfile> findByUserId(Long userId);


}
