CREATE TABLE interview(
id BIGINT PRIMARY KEY AUTO_INCREMENT,
recruitment_id BIGINT NOT NULL,
round_name VARCHAR(100) NOT NULL,
interview_type VARCHAR(40) NOT NULL,
scheduled_at DATETIME(6) NOT NULL,
duration_minutes INT NOT NULL,
timezone VARCHAR(50) NOT NULL,
mode VARCHAR(20) NOT NULL,
meeting_link VARCHAR(1000),
location VARCHAR(500),
status VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED',
instructions VARCHAR(2000),
created_by BIGINT NOT NULL,
created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
    ON UPDATE CURRENT_TIMESTAMP(6),

CONSTRAINT fk_interview_recruitment
    FOREIGN KEY (recruitment_id)
    REFERENCES application_recruitment(id)
    ON DELETE RESTRICT,

CONSTRAINT fk_interview_created_by
    FOREIGN KEY (created_by)
    REFERENCES users(id)
    ON DELETE RESTRICT,

CONSTRAINT chk_interview_duration
    CHECK (duration_minutes > 0),

INDEX idx_interview_recruitment (recruitment_id),
INDEX idx_interview_scheduled_at (scheduled_at),
INDEX idx_interview_status(status)
);