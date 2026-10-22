package com.jobPortal.careerBridge.controller;


import com.jobPortal.careerBridge.service.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private CurrentUserService currentUserService;

    @Autowired
    public TestController(CurrentUserService currentUserService){
        this.currentUserService = currentUserService;
    }

    @GetMapping("/protected")
    public String protectedEndpoint() {
        Long userId = currentUserService.getCurrentUserId();
        return "Authenticated User ID: "+userId;
    }

    @GetMapping("/job-create")
    @PreAuthorize("hasAuthority('JOB_CREATE')")
    public ResponseEntity<String> testJobCreate() {
        System.out.println("JOB-CREATE METHOD EXECUTED");

        return ResponseEntity.status(HttpStatus.OK)
                .body("You have JOB_CREATE permission");
    }



}
