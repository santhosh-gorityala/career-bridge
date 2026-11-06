CREATE TABLE companies(
id BIGINT PRIMARY KEY AUTO_INCREMENT,
owner_user_id BIGINT NOT NULL,

name VARCHAR(255) NOT NULL,
description TEXT,
website VARCHAR(500),
industry VARCHAR(100),
company_size VARCHAR(50),
location VARCHAR(255),

logo_storage_key VARCHAR(500),

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
ON UPDATE CURRENT_TIMESTAMP,

CONSTRAINT fk_companies_owner
    FOREIGN KEY (owner_user_id)
    REFERENCES users(id)
    ON DELETE RESTRICT
);

CREATE INDEX idx_companies_owner_user_id
ON companies(owner_user_id);