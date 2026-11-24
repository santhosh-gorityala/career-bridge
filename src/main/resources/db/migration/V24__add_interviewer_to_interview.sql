-- Add interviewer column
ALTER TABLE interview
ADD COLUMN interviewer_id BIGINT NULL;

-- Adding foreign key
ALTER TABLE interview
ADD CONSTRAINT fk_interview_interviewer
FOREIGN KEY (interviewer_id)
REFERENCES users(id)
ON DELETE RESTRICT;


-- Adding Index
CREATE INDEX idx_interview_interviewer
ON interview(interviewer_id);

-- Assign existing interviews to their creators
UPDATE interview
SET interviewer_id = created_by
WHERE interviewer_id IS NULL;

-- Make interviewer mandatory
ALTER TABLE interview
MODIFY COLUMN interviewer_id BIGINT NOT NULL;