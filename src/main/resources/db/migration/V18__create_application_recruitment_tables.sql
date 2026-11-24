

-- Table 1: Stores the current recruitment stage
CREATE TABLE application_recruitment(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
application_id BIGINT NOT NULL UNIQUE,
current_stage VARCHAR(40) NOT NULL,
started_by BIGINT NOT NULL,
started_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
ON UPDATE CURRENT_TIMESTAMP,
version BIGINT NOT NULL DEFAULT 0,

CONSTRAINT fk_recruitment_application
    FOREIGN KEY (application_id)
    REFERENCES applications(id)
    ON DELETE RESTRICT,

CONSTRAINT fk_recruitment_started_by
    FOREIGN KEY (started_by)
    REFERENCES users(id)
    ON DELETE RESTRICT
);

-- Table 2: Stores recruitment stage history

CREATE TABLE application_stage_history(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
recruitment_id BIGINT NOT NULL,
from_stage VARCHAR(40) NULL,
to_stage VARCHAR(40) NOT NULL,
changed_by BIGINT NOT NULL,
reason VARCHAR(100) NULL,
changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_stage_history_recruitment
    FOREIGN KEY (changed_by)
    REFERENCES application_recruitment(id)
    ON DELETE RESTRICT,

CONSTRAINT fk_stage_history_changed_by
    FOREIGN KEY (changed_by)
    REFERENCES users(id)
    ON DELETE RESTRICT,

INDEX idx_recruitment_history
    (recruitment_id,changed_by)
);