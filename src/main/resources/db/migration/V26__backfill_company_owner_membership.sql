INSERT INTO company_memberships(
company_id,
users_id,
status,
joined_at
)
SELECT
c.id,
c.owner_user_id,
'ACTIVE',
CURRENT_TIMESTAMP(6)
FROM companies c
WHERE NOT EXISTS (
SELECT 1
FROM company_memberships cm
WHERE cm.company_id = c.id
AND cm.users_id = c.owner_user_id
);