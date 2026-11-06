CREATE TABLE jobs(
id BIGINT PRIMARY KEY AUTO_INCREMENT,

company_id BIGINT NOT NULL,
created_by BIGINT NOT NULL,

title VARCHAR(255),
description TEXT,

employment_type VARCHAR(30),
workplace_type VARCHAR(30),

location VARCHAR(255),

experience_min INT,
experience_max INT,

salary_min DECIMAL(12,2),
salary_max DECIMAL(12,2),

vacancies INT NOT NULL DEFAULT 1,

status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',

application_deadline DATE,

published_at TIMESTAMP NULL,

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
ON UPDATE CURRENT_TIMESTAMP,

CONSTRAINT fk_jobs_company
       FOREIGN KEY (company_id)
       REFERENCES companies(id)
       ON DELETE RESTRICT,

       CONSTRAINT fk_jobs_creator
       FOREIGN KEY (created_by)
       REFERENCES users(id)
       ON DELETE RESTRICT

);

CREATE INDEX idx_jobs_company_id
ON jobs(company_id);

CREATE INDEX idx_jobs_created_by
ON jobs(created_by);

CREATE INDEX idx_jobs_status
ON jobs(status);

CREATE INDEX idx_jobs_created_at
ON jobs(created_at);