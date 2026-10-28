CREATE TABLE skills (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE user_profile_skills (
    profile_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,

    PRIMARY KEY (profile_id, skill_id),

    CONSTRAINT fk_user_profile_skills_profile
        FOREIGN KEY (profile_id)
        REFERENCES user_profiles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_profile_skills_skill
        FOREIGN KEY (skill_id)
        REFERENCES skills(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_user_profile_skills_skill_id
    ON user_profile_skills(skill_id);