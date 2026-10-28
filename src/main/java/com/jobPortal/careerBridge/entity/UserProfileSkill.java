package com.jobPortal.careerBridge.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profile_skills",
uniqueConstraints = {
        @UniqueConstraint(
                name="uk_user_profile_skill",
                columnNames = {"profile_id","skill_id"}
        )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserProfileSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="profile_id",nullable = false)
    private UserProfile profile;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "skill_id",nullable = false)
    private Skill skill;

    public UserProfileSkill(
            UserProfile profile,
            Skill skill
    ){
        this.profile = profile;
        this.skill = skill;
    }
}
