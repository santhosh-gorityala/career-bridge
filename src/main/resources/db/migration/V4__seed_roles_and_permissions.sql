INSERT INTO permissions (name) VALUES
('JOB_VIEW'),
('JOB_CREATE'),
('JOB_UPDATE'),
('JOB_DELETE'),
('JOB_APPLY'),
('APPLICATION_VIEW'),
('APPLICATION_UPDATE'),
('USER_VIEW'),
('USER_UPDATE'),
('USER_MANAGE'),
('COMPANY_CREATE'),
('COMPANY_UPDATE'),
('COMPANY_VIEW'),
('RECRUITMENT_MANAGE');

INSERT INTO roles (name) VALUES
('JOB_SEEKER'),
('RECRUITER'),
('HIRING_MANAGER'),
('ADMIN');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p
WHERE r.name = 'JOB_SEEKER'
  AND p.name IN (
      'JOB_VIEW',
      'JOB_APPLY',
      'APPLICATION_VIEW',
      'USER_VIEW',
      'USER_UPDATE',
      'COMPANY_VIEW'
  );

  INSERT INTO role_permissions (role_id, permission_id)
  SELECT r.id, p.id
  FROM roles r
  JOIN permissions p
  WHERE r.name = 'RECRUITER'
    AND p.name IN (
        'JOB_VIEW',
        'JOB_CREATE',
        'JOB_UPDATE',
        'JOB_DELETE',
        'APPLICATION_VIEW',
        'APPLICATION_UPDATE',
        'COMPANY_VIEW',
        'COMPANY_CREATE',
        'COMPANY_UPDATE',
        'RECRUITMENT_MANAGE'
    );

    INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.id, p.id
    FROM roles r
    JOIN permissions p
    WHERE r.name = 'HIRING_MANAGER'
      AND p.name IN (
          'JOB_VIEW',
          'APPLICATION_VIEW',
          'APPLICATION_UPDATE',
          'COMPANY_VIEW',
          'RECRUITMENT_MANAGE'
      );


INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'ADMIN';