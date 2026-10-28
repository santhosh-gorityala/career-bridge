package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.request.UserProfileRequest;
import com.jobPortal.careerBridge.dto.response.UserProfileResponse;
import com.jobPortal.careerBridge.entity.UserProfile;
import com.jobPortal.careerBridge.exception.ResourceNotFoundException;
import com.jobPortal.careerBridge.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final CurrentUserService currentUserService;
    @Autowired
    public UserProfileService(
            UserProfileRepository userProfileRepository,
            CurrentUserService currentUserService
    ){
        this.userProfileRepository = userProfileRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile =
                userProfileRepository.findByUserId(userId).
                orElseThrow(()-> new ResourceNotFoundException(
                        "Profile not found"
                ));
        return new UserProfileResponse(
          userProfile.getId(),
          userProfile.getHeadline(),
          userProfile.getBio(),
          userProfile.getLocation()
        );
    }

    @Transactional
    public UserProfileResponse updateMyProfile(
            UserProfileRequest request
    ){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile =
                userProfileRepository.findByUserId(userId).
                orElseThrow(()-> new ResourceNotFoundException(
                        "Profile not found"
                ));
        userProfile.update(
                request.getHeadline(),
                request.getBio(),
                request.getLocation()
        );
        return new UserProfileResponse(
                userProfile.getId(),
                userProfile.getHeadline(),
                userProfile.getBio(),
                userProfile.getLocation()
        );
    }




}
