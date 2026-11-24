INSERT INTO permissions(name)
    VALUES ('INTERVIEW_CANDIDATE_VIEW');

INSERT INTO role_permissions(role_id,permission_id)
SELECT r.id,p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name IN ('JOB_SEEKER','ADMIN')
AND p.name = 'INTERVIEW_CANDIDATE_VIEW';
