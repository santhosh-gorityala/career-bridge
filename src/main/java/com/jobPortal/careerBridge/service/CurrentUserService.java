package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public CustomUserDetails getCurrentUser(){
        Authentication authentication =
                SecurityContextHolder.getContext().
                        getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }

    public Long getCurrentUserId(){
        return getCurrentUser().getUserId();
    }

    public String getCurrentUserEmail(){
        return getCurrentUser().getEmail();
    }


}
