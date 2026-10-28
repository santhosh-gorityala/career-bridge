package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.response.SkillResponse;
import com.jobPortal.careerBridge.entity.Skill;
import com.jobPortal.careerBridge.entity.UserProfile;
import com.jobPortal.careerBridge.entity.UserProfileSkill;
import com.jobPortal.careerBridge.exception.ResourceNotFoundException;
import com.jobPortal.careerBridge.exception.SkillAlreadyAddedException;
import com.jobPortal.careerBridge.repository.SkillRepository;
import com.jobPortal.careerBridge.repository.UserProfileRepository;
import com.jobPortal.careerBridge.repository.UserProfileSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserProfileSkillService {
    private final UserProfileSkillRepository userProfileSkillRepository;
    private final CurrentUserService currentUserService;
    private final UserProfileRepository userProfileRepository;
    private final SkillRepository skillRepository;
    @Autowired
    public UserProfileSkillService(
            UserProfileSkillRepository userProfileSkillRepository,
            CurrentUserService currentUserService,
            UserProfileRepository userProfileRepository,
            SkillRepository skillRepository
    ){
        this.userProfileSkillRepository = userProfileSkillRepository;
        this.currentUserService = currentUserService;
        this.userProfileRepository = userProfileRepository;
        this.skillRepository = skillRepository;
    }

    @Transactional
    public void addSkill(Long skillId){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile profile =
                userProfileRepository.findByUserId(userId).orElseThrow(
                ()-> new ResourceNotFoundException("Profile not found")
        );
        Skill skill= skillRepository.findById(skillId).
                orElseThrow(()->
                        new ResourceNotFoundException("Skill not found"));
        if(userProfileSkillRepository.
                existsByProfileIdAndSkillId(profile.getId(),skill.getId())
        ){
            throw new SkillAlreadyAddedException("Skill is already added to your profile");
        }

        UserProfileSkill userProfileSkill =
                new UserProfileSkill(profile,skill);
        userProfileSkillRepository.save(userProfileSkill);

    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getMySkills(){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile profile = userProfileRepository.findByUserId(userId).orElseThrow(
                ()-> new ResourceNotFoundException("Profile not found")
        );
        List<UserProfileSkill> profileSkills =
                userProfileSkillRepository.findByProfileId(profile.getId());
        List<SkillResponse> skillResponseList = new ArrayList<>();
        for(UserProfileSkill profileSkill : profileSkills){
            Skill skill = profileSkill.getSkill();
            skillResponseList.add(
                    new SkillResponse(
                            skill.getId(),skill.getName()
                    )
            );
        }
        return skillResponseList;
    }

    @Transactional
    public void deleteMySkill(Long skillId){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile =
                userProfileRepository.findByUserId(userId).
                orElseThrow(()-> new ResourceNotFoundException("Profile not found"));
        boolean exists = userProfileSkillRepository.
                existsByProfileIdAndSkillId(
                userProfile.getId(),
                skillId
        );
        if(!exists){
            throw new ResourceNotFoundException(
                    "Skill is not added to your profile"
            );
        }
        userProfileSkillRepository.deleteByProfileIdAndSkillId(
                userProfile.getId(),
                skillId
        );
    }


}
