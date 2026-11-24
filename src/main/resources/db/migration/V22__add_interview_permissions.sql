-- 1. Create interview permissions
INSERT INTO permissions (name)
VALUES
    ('INTERVIEW_CREATE'),
    ('INTERVIEW_VIEW'),
    ('INTERVIEW_UPDATE'),
    ('INTERVIEW_FEEDBACK_SUBMIT');


-- 2. Assign interview permissions to RECRUITER,
-- HIRING MANAGER and ADMIN

INSERT INTO role_permissions(role_id,permission_id)
SELECT r.id,p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name IN ('RECRUITER','HIRING_MANAGER','ADMIN')
AND p.name IN (
'INTERVIEW_CREATE',
'INTERVIEW_VIEW',
'INTERVIEW_UPDATE',
'INTERVIEW_FEEDBACK_SUBMIT'
);