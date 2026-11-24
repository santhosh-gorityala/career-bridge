
-- Correct the composite index
DROP INDEX idx_recruitment_history
ON application_stage_history;

CREATE INDEX idx_recruitment_history
ON application_stage_history (recruitment_id, changed_at);

-- Increase the reason column length
ALTER TABLE application_stage_history
MODIFY COLUMN reason VARCHAR(1000) NULL;
