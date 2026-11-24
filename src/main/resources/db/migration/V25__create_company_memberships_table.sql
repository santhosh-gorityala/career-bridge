CREATE TABLE company_memberships (
id BIGINT PRIMARY KEY AUTO_INCREMENT,

company_id BIGINT NOT NULL,
users_id BIGINT NOT NULL,

status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

joined_at DATETIME(6) NULL,

created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

CONSTRAINT uq_company_company_memberships
    UNIQUE(company_id,users_id),

CONSTRAINT fk_company_membership_company
    FOREIGN KEY (company_id)
    REFERENCES companies(id)
    ON DELETE RESTRICT,

CONSTRAINT fk_company_membership_user
    FOREIGN KEY (users_id)
    REFERENCES users(id)
    ON DELETE RESTRICT,

CONSTRAINT chk_company_membership_status
    CHECK (status IN ('ACTIVE','INVITED','REMOVED')),

INDEX idx_company_membership_user (users_id),
INDEX idx_company_membership_company_status (company_id,status)

);