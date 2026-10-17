package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.RegistrationRequest;
import com.jobPortal.careerBridge.dto.UserResponse;
import com.jobPortal.careerBridge.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
           @Valid @RequestBody RegistrationRequest request){
        UserResponse response =
                authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
