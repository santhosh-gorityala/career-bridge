package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
// @RequiredArgsConstructor and make "final private UserRepository userRepository;"
// done no need of constructor
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }




}
