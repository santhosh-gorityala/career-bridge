CREATE TABLE applications (
id BIGINT PRIMARY KEY AUTO_INCREMENT,

job_id BIGINT NOT NULL,
applicant_user_id BIGINT NOT NULL,

resume_file_name VARCHAR(255) NOT NULL,
resume_storage_key VARCHAR(500) NOT NULL UNIQUE,
resume_content_type VARCHAR(100) NOT NULL,
resume_file_size BIGINT NOT NULL,

cover_letter TEXT,

status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED',

applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

CONSTRAINT fk_applications_job
    FOREIGN KEY (job_id)
    REFERENCES jobs(id)
    ON DELETE RESTRICT,

CONSTRAINT fk_applications_applicant
    FOREIGN KEY (applicant_user_id)
    REFERENCES users(id)
    ON DELETE RESTRICT,

CONSTRAINT uk_application_job_applicant
    UNIQUE (job_id,applicant_user_id),

CONSTRAINT chk_application_status
    CHECK( status IN (
    'SUBMITTED',
    'UNDER_REVIEW',
    'SHORTLISTED',
    'REJECTED',
    'WITHDRAW'
)),

CONSTRAINT chk_application_resume_size
    CHECK (resume_file_size > 0)
);

CREATE INDEX id_applications_job_id
    ON applications(job_id);

CREATE INDEX idx_applications_applicant_user_id
    ON applications(applicant_user_id);

CREATE INDEX idx_applications_job_status
    ON applications(job_id,status);

CREATE INDEX idx_applications_applicant_applied_at
    ON applications(applicant_user_id,applied_at);