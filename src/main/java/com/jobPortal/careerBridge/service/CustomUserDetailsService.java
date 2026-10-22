package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.entity.User;
import com.jobPortal.careerBridge.repository.UserRepository;
import com.jobPortal.careerBridge.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public CustomUserDetails loadUserById(Long userId) {

        User user = userRepository.findByIdWithRolesAndPermissions(userId)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow(
                ()->
                        new UsernameNotFoundException("User not found")
        );
        return new CustomUserDetails(user);
    }


}
