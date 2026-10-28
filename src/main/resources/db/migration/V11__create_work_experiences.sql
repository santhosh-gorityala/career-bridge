CREATE TABLE work_experiences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    profile_id BIGINT NOT NULL,

    company_name VARCHAR(255) NOT NULL,
    job_title VARCHAR(255) NOT NULL,

    employment_type VARCHAR(50),

    start_date DATE NOT NULL,
    end_date DATE,

    currently_working BOOLEAN NOT NULL DEFAULT FALSE,

    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_work_experiences_profile
        FOREIGN KEY (profile_id)
        REFERENCES user_profiles(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_work_experiences_profile_id
    ON work_experiences(profile_id);