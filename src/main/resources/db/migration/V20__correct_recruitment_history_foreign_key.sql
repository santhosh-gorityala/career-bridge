
-- Remove the incorrectly created foreign key
ALTER TABLE application_stage_history
DROP FOREIGN KEY fk_stage_history_recruitment;

-- Add the correct foreign key
ALTER TABLE application_stage_history
ADD CONSTRAINT fk_stage_history_recruitment
FOREIGN KEY (recruitment_id)
REFERENCES application_recruitment(id)
ON DELETE RESTRICT;
