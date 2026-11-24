ALTER TABLE applications
DROP CHECK chk_application_status;

ALTER TABLE applications
ADD CONSTRAINT chk_application_status
CHECK (status IN (
    'SUBMITTED',
    'UNDER_REVIEW',
    'SHORTLISTED',
    'REJECTED',
    'WITHDRAWN'
));