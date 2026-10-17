package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.RegistrationRequest;
import com.jobPortal.careerBridge.dto.UserResponse;
import com.jobPortal.careerBridge.entity.User;
import com.jobPortal.careerBridge.entity.UserStatus;
import com.jobPortal.careerBridge.exception.EmailAlreadyExistsException;
import com.jobPortal.careerBridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegistrationRequest request){
        String email = request.getEmail();
        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException("Email is already registered");
        }
        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = new User(
        request.getEmail(),
        passwordHash,
        request.getFirstName(),
        request.getLastName(),
        request.getPhone(),
        UserStatus.ACTIVE,
        false,
        LocalDateTime.now(),
        LocalDateTime.now()
        );

        userRepository.save(user);
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone()
        );
    }




}
