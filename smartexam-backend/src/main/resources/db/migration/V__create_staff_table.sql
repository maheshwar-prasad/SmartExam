CREATE TABLE staff (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    staff_code VARCHAR(50) NOT NULL,
    employee_code VARCHAR(50) NULL,

    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100) NULL,
    last_name VARCHAR(100) NOT NULL,

    gender VARCHAR(20) NULL,
    date_of_birth DATE NULL,

    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    alternate_phone VARCHAR(20) NULL,

    profile_photo_url VARCHAR(500) NULL,

    designation VARCHAR(150) NOT NULL,
    department_id BIGINT NULL,

    staff_type VARCHAR(30) NOT NULL,
    employment_type VARCHAR(30) NOT NULL,

    joining_date DATE NOT NULL,

    qualification VARCHAR(255) NULL,
    experience_years DECIMAL(5,2) NULL,

    address VARCHAR(500) NULL,
    city VARCHAR(100) NULL,
    state VARCHAR(100) NULL,
    country VARCHAR(100) NULL,
    postal_code VARCHAR(20) NULL,

    emergency_contact_name VARCHAR(150) NULL,
    emergency_contact_phone VARCHAR(20) NULL,
    emergency_contact_relation VARCHAR(50) NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT uk_staff_organization_staff_code
        UNIQUE (organization_id, staff_code),

    CONSTRAINT uk_staff_organization_employee_code
        UNIQUE (organization_id, employee_code),

    CONSTRAINT uk_staff_organization_email
        UNIQUE (organization_id, email)
);

<!-- after table creation -->
CREATE INDEX idx_staff_organization_id
    ON staff (organization_id);

CREATE INDEX idx_staff_department_id
    ON staff (department_id);

CREATE INDEX idx_staff_status
    ON staff (status);

CREATE INDEX idx_staff_active
    ON staff (active);

CREATE INDEX idx_staff_name
    ON staff (first_name, last_name);