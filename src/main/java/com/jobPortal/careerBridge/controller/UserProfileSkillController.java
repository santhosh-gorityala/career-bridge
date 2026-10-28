package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.response.SkillResponse;
import com.jobPortal.careerBridge.entity.Skill;
import com.jobPortal.careerBridge.service.UserProfileSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile/skills")
public class UserProfileSkillController {

    private final UserProfileSkillService userProfileSkillService;

    @Autowired
    public UserProfileSkillController(
            UserProfileSkillService userProfileSkillService
    ){
        this.userProfileSkillService = userProfileSkillService;
    }

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getMySkills(){
        List<SkillResponse> profileSkills =
                userProfileSkillService.getMySkills();
        return ResponseEntity.status(HttpStatus.OK).body(profileSkills);
    }

    @PostMapping("/{skillId}")
    public ResponseEntity<Void> addSkill(
        @PathVariable Long skillId
    ){
        userProfileSkillService.addSkill(skillId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> deleteSkill(
            @PathVariable Long skillId
    ){
        userProfileSkillService.deleteMySkill(skillId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
