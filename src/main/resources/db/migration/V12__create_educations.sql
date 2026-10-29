CREATE TABLE educations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    profile_id BIGINT NOT NULL,

    degree VARCHAR(255) NOT NULL,
    institution VARCHAR(255) NOT NULL,
    field_of_study VARCHAR(255),

    start_date DATE NOT NULL,
    end_date DATE,

    currently_studying BOOLEAN NOT NULL DEFAULT FALSE,

    grade VARCHAR(50),

    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_educations_profile
        FOREIGN KEY (profile_id)
        REFERENCES user_profiles(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_educations_profile_id
    ON educations(profile_id);