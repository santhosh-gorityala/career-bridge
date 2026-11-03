CREATE TABLE resumes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    profile_id BIGINT NOT NULL UNIQUE,

    file_name VARCHAR(255) NOT NULL,

    storage_key VARCHAR(500) NOT NULL UNIQUE,

    content_type VARCHAR(100) NOT NULL,

    file_size BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_resumes_profile
        FOREIGN KEY (profile_id)
        REFERENCES user_profiles(id)
        ON DELETE CASCADE
);
