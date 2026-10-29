package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.request.UserEducationRequest;
import com.jobPortal.careerBridge.dto.response.EducationResponse;
import com.jobPortal.careerBridge.entity.Education;
import com.jobPortal.careerBridge.entity.UserProfile;
import com.jobPortal.careerBridge.exception.InvalidEducationException;
import com.jobPortal.careerBridge.exception.ResourceNotFoundException;
import com.jobPortal.careerBridge.repository.EducationRepository;
import com.jobPortal.careerBridge.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final CurrentUserService currentUserService;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public EducationService(
            EducationRepository educationRepository,
            CurrentUserService currentUserService,
            UserProfileRepository userProfileRepository
    ){
        this.educationRepository = educationRepository;
        this.currentUserService = currentUserService;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public EducationResponse addEducation(
            UserEducationRequest request
    ){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile =
                userProfileRepository.findByUserId(userId).
                orElseThrow(()->
                        new ResourceNotFoundException("Profile not found")
                        );
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        boolean isCurrentlyStudying = request.isCurrentlyStudying();

        validateEducationDates(
                startDate,
                endDate,
                isCurrentlyStudying
        );

        LocalDateTime now = LocalDateTime.now();

        Education education =
                new Education(
                        userProfile,
                        request.getDegree(),
                        request.getInstitution(),
                        request.getFieldOfStudy(),
                        startDate,
                        endDate,
                        isCurrentlyStudying,
                        request.getGrade(),
                        request.getDescription(),
                        now,
                        now
                );
        Education savedEducation =
                educationRepository.save(education);

        return new EducationResponse(
                savedEducation.getId(),
                savedEducation.getDegree(),
                savedEducation.getInstitution(),
                savedEducation.getFieldOfStudy(),
                savedEducation.getStartDate(),
                savedEducation.getEndDate(),
                savedEducation.isCurrentlyStudying(),
                savedEducation.getGrade(),
                savedEducation.getDescription()
        );
    }

    @Transactional(readOnly = true)
    public List<EducationResponse> getMyEducation(){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile =
                userProfileRepository.findByUserId(userId).
                orElseThrow(
                        ()-> new ResourceNotFoundException(
                                "Profile not found"
                        )
                );
        List<Education> educationList =
                educationRepository.
                        findByProfileId(userProfile.getId());
        List<EducationResponse> educationResponses = new ArrayList<>();
        for(Education education : educationList){
            educationResponses.add(
                    new EducationResponse(
                     education.getId(),
                     education.getDegree(),
                     education.getInstitution(),
                     education.getFieldOfStudy(),
                     education.getStartDate(),
                     education.getEndDate(),
                     education.isCurrentlyStudying(),
                     education.getGrade(),
                     education.getDescription()
                    )
            );
        }
        return educationResponses;
    }

    @Transactional
    public EducationResponse updateMyEducation(
            Long educationId, UserEducationRequest request
    ){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile = userProfileRepository.
                findByUserId(userId).
                orElseThrow(
                        ()-> new ResourceNotFoundException(
                                "Profile not found"
                        )
                );
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        boolean isCurrentlyStudying = request.isCurrentlyStudying();
        validateEducationDates(
                startDate,
                endDate,
                isCurrentlyStudying
        );
        Education education =
                educationRepository.findByIdAndProfileId(
                educationId,
                userProfile.getId()
        ).orElseThrow(
                ()->new ResourceNotFoundException(
                        "Education not found"
                )
        );
        education.update(
                request.getDegree(),
                request.getInstitution(),
                request.getFieldOfStudy(),
                startDate,
                endDate,
                isCurrentlyStudying,
                request.getGrade(),
                request.getDescription()
        );

        return new EducationResponse(
                education.getId(),
                education.getDegree(),
                education.getInstitution(),
                education.getFieldOfStudy(),
                education.getStartDate(),
                education.getEndDate(),
                education.isCurrentlyStudying(),
                education.getGrade(),
                education.getDescription()
        );
    }

    @Transactional
    public void deleteMyEducation(
            Long educationId
    ){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile = userProfileRepository.
                findByUserId(userId).
                orElseThrow(
                        ()-> new ResourceNotFoundException(
                                "Profile not found"
                        )
                );
        Education education =
                educationRepository.findByIdAndProfileId(
                educationId,userProfile.getId()
        ).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Education not found"
                )
        );
        educationRepository.delete(education);
    }

    private void validateEducationDates(
            LocalDate startDate,
            LocalDate endDate,
            boolean isCurrentlyStudying
    ){

        if(endDate != null && endDate.isBefore(startDate)){
            throw new InvalidEducationException(
                    "End date cannot be before start date"
            );
        }
        if(isCurrentlyStudying && endDate != null){
            throw new InvalidEducationException(
                    "End date must be null when currently studying"
            );
        }

        if(!isCurrentlyStudying && endDate == null){
            throw  new InvalidEducationException(
                    "End date is required when not currently studying"
            );
        }
    }


}
