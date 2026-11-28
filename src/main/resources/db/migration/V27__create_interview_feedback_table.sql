CREATE TABLE interview_feedback (
id BIGINT NOT NULL AUTO_INCREMENT,

interview_id BIGINT NOT NULL,

overall_rating INT NOT NULL,
technical_rating INT NOT NULL,
communication_rating INT NOT NULL,
problem_solving_rating INT NOT NULL,

strengths TEXT NOT NULL,
areas_to_improve TEXT NOT NULL,
comments TEXT NULL,

recommendation VARCHAR(30) NOT NULL,

submitted_by BIGINT NOT NULL,
updated_by BIGINT NULL,

submitted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
  ON UPDATE CURRENT_TIMESTAMP,

CONSTRAINT pk_interview_feedback
    PRIMARY KEY (id),

CONSTRAINT uk_interview_feedback_interview
    UNIQUE (interview_id),

CONSTRAINT fk_interview_feedback_interview
    FOREIGN KEY (interview_id)
    REFERENCES interview(id),

CONSTRAINT fk_interview_feedback_submitted_by
    FOREIGN KEY (submitted_by)
    REFERENCES users(id),

CONSTRAINT fk_interview_feedback_updated_by
    FOREIGN KEY (updated_by)
    REFERENCES users(id),

CONSTRAINT chk_feedback_overall_rating
    CHECK (overall_rating BETWEEN 1 AND 5),

CONSTRAINT chk_feedback_technical_rating
    CHECK (technical_rating BETWEEN 1 AND 5),

CONSTRAINT chk_feedback_communication_rating
    CHECK (communication_rating BETWEEN 1 AND 5),

CONSTRAINT chk_feedback_problem_solving_rating
    CHECK (problem_solving_rating BETWEEN 1 AND 5),

CONSTRAINT chk_feedback_recommendation
    CHECK (
        recommendation IN (
        'STRONG_YES',
        'YES',
        'MAYBE',
        'NO',
        'STRONG_NO'
        )
    )


);