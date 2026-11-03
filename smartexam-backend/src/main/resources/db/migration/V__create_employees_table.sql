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
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    created_by BIGINT NULL,
    updated_at DATETIME(6) NULL,
    updated_by BIGINT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_employee_code_org
        UNIQUE (organization_id, employee_code),

    CONSTRAINT uk_employee_user_org
        UNIQUE (organization_id, user_id),

    CONSTRAINT fk_employee_organization
        FOREIGN KEY (organization_id) REFERENCES organizations(id),

    CONSTRAINT fk_employee_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT chk_employee_type
        CHECK (employee_type IN (
            'TEACHER',
            'ADMINISTRATIVE',
            'SUPPORT',
            'OTHER'
        )),

    INDEX idx_employee_org (organization_id),
    INDEX idx_employee_type (employee_type),
    INDEX idx_employee_active (active)
);