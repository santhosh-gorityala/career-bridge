package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.request.UserWorkExperienceRequest;
import com.jobPortal.careerBridge.dto.response.WorkExperienceResponse;
import com.jobPortal.careerBridge.entity.UserProfile;
import com.jobPortal.careerBridge.entity.WorkExperience;
import com.jobPortal.careerBridge.exception.InvalidWorkExperienceException;
import com.jobPortal.careerBridge.exception.ResourceNotFoundException;
import com.jobPortal.careerBridge.repository.UserProfileRepository;
import com.jobPortal.careerBridge.repository.WorkExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final CurrentUserService currentUserService;
    private final UserProfileRepository userProfileRepository;
    @Autowired
    public WorkExperienceService(
            WorkExperienceRepository workExperienceRepository,
            CurrentUserService currentUserService,
            UserProfileRepository userProfileRepository
    ){
        this.workExperienceRepository = workExperienceRepository;
        this.currentUserService = currentUserService;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public WorkExperienceResponse addExperience(
            UserWorkExperienceRequest experienceRequest
    ){

        Long userId = currentUserService.getCurrentUserId();

        UserProfile userProfile =
                userProfileRepository.findByUserId(userId).
                orElseThrow(
                        ()-> new ResourceNotFoundException("Profile not found")
                        );
        WorkExperience workExperience =
                new WorkExperience(
                  userProfile,
                  experienceRequest.getCompanyName(),
                  experienceRequest.getJobTitle(),
                  experienceRequest.getEmploymentType(),
                  experienceRequest.getStartDate(),
                  experienceRequest.getEndDate(),
                  experienceRequest.isCurrentlyWorking(),
                  experienceRequest.getDescription(),
                  LocalDateTime.now(),
                  LocalDateTime.now()
                );

        WorkExperience savedWorkExperience =
                workExperienceRepository.save(workExperience);

        WorkExperienceResponse experienceResponse =
                new WorkExperienceResponse(
                  savedWorkExperience.getId(),
                  savedWorkExperience.getCompanyName(),
                  savedWorkExperience.getJobTitle(),
                  savedWorkExperience.getEmploymentType(),
                  savedWorkExperience.getStartDate(),
                  savedWorkExperience.getEndDate(),
                  savedWorkExperience.isCurrentyWorking(),
                  savedWorkExperience.getDescription()
                );

        return experienceResponse;
    }

    @Transactional(readOnly = true)
    public List<WorkExperienceResponse> getMyExperience(){
    Long userId = currentUserService.getCurrentUserId();
    UserProfile userProfile =
            userProfileRepository.findByUserId(userId).
            orElseThrow(()-> new ResourceNotFoundException("Profile not found"));
    List<WorkExperience> workExperiences =
            workExperienceRepository.
            findByProfileId(userProfile.getId());
    List<WorkExperienceResponse> responseList = new ArrayList<>();
    for(WorkExperience experience : workExperiences){
        WorkExperienceResponse response =
                new WorkExperienceResponse(
                        experience.getId(),
                        experience.getCompanyName(),
                        experience.getJobTitle(),
                        experience.getEmploymentType(),
                        experience.getStartDate(),
                        experience.getEndDate(),
                        experience.isCurrentyWorking(),
                        experience.getDescription()
                );
        responseList.add(response);
    }
    return responseList;
    }

    @Transactional
    public WorkExperienceResponse updateMyExperience(
            Long experienceId,
            UserWorkExperienceRequest request
    ){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile =
                userProfileRepository.findByUserId(userId).
                orElseThrow(()-> new ResourceNotFoundException("Profile not found"));
        boolean isCurrentlyWorking = request.isCurrentlyWorking();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        validateExperienceDates(startDate,endDate);
        WorkExperience workExperience =
                workExperienceRepository.
                        findByIdAndProfileId(
                                experienceId,userProfile.getId()
                        ).
                        orElseThrow(()->
                                new ResourceNotFoundException(
                                        "Work experience not found"
                                )
                        );


        if(!isCurrentlyWorking && endDate == null){
            throw new InvalidWorkExperienceException(
                    "End date is required when not currently working"
            );
        }
        if(isCurrentlyWorking && endDate != null){
            throw new InvalidWorkExperienceException(
                    "End date must be null when currently working"
            );
        }




        workExperience.updateMyExperience(
                request.getCompanyName(),
                request.getJobTitle(),
                request.getEmploymentType(),
                request.getStartDate(),
                request.getEndDate(),
                request.isCurrentlyWorking(),
                request.getDescription()
        );

        return new WorkExperienceResponse(
                workExperience.getId(),
                workExperience.getCompanyName(),
                workExperience.getJobTitle(),
                workExperience.getEmploymentType(),
                workExperience.getStartDate(),
                workExperience.getEndDate(),
                workExperience.isCurrentyWorking(),
                workExperience.getDescription()
        );
    }

    @Transactional
    public void deleteMyExperience(
            Long experienceId
    ){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile =
                userProfileRepository.findByUserId(userId).
                orElseThrow(
                        ()-> new ResourceNotFoundException(
                                "Profile not found"
                        )
                );
        WorkExperience workExperience =
                workExperienceRepository.
                findByIdAndProfileId(
                        experienceId,
                        userProfile.getId()
                        ).
                orElseThrow(
                        ()-> new ResourceNotFoundException(
                                "work experience not found"
                        )
                );
        workExperienceRepository.delete(workExperience);
    }

    private void validateExperienceDates(
            LocalDate startDate,
            LocalDate endDate) {

        if (endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidWorkExperienceException(
                    "End date cannot be before start date"
            );
        }
    }



}
