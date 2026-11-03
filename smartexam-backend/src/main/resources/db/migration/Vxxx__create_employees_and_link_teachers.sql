-- 1. Create the shared employee master table.
CREATE TABLE employees (
    id BIGINT NOT NULL AUTO_INCREMENT,
    organization_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    employee_code VARCHAR(30) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    employee_type VARCHAR(30) NOT NULL,

    gender VARCHAR(20) NULL,
    date_of_birth DATE NULL,
    joining_date DATE NOT NULL,

    email VARCHAR(150) NULL,
    mobile VARCHAR(20) NULL,
    address VARCHAR(500) NULL,

    created_at DATETIME(6) NOT NULL,
    created_by BIGINT NULL,
    updated_at DATETIME(6) NULL,
    updated_by BIGINT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    CONSTRAINT uk_employee_org_code
        UNIQUE (organization_id, employee_code),

    CONSTRAINT uk_employee_org_user
        UNIQUE (organization_id, user_id),

    CONSTRAINT fk_employee_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT fk_employee_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE INDEX idx_employee_org
    ON employees(organization_id);

CREATE INDEX idx_employee_type
    ON employees(employee_type);

CREATE INDEX idx_employee_active
    ON employees(active);

-- 2. Add a nullable link to the existing teachers table.
-- Existing teacher records remain valid during migration.
ALTER TABLE teachers
    ADD COLUMN employee_id BIGINT NULL;

CREATE UNIQUE INDEX uk_teacher_employee
    ON teachers(employee_id);

ALTER TABLE teachers
    ADD CONSTRAINT fk_teacher_employee
    FOREIGN KEY (employee_id)
    REFERENCES employees(id);