CREATE TABLE courses (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,
    campus_id BIGINT NULL,
    department_id BIGINT NULL,
    program_id BIGINT NULL,

    name VARCHAR(150) NOT NULL,
    code VARCHAR(50) NOT NULL,
    description VARCHAR(1000) NULL,

    credits DECIMAL(5,2) NULL,
    semester INT NULL,

    course_type VARCHAR(30) NOT NULL DEFAULT 'GENERAL',
    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_courses_organization_code
        UNIQUE (organization_id, code),

    INDEX idx_courses_organization_id (organization_id),
    INDEX idx_courses_campus_id (campus_id),
    INDEX idx_courses_department_id (department_id),
    INDEX idx_courses_program_id (program_id),
    INDEX idx_courses_name (name),

    CONSTRAINT fk_courses_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT fk_courses_campus
        FOREIGN KEY (campus_id)
        REFERENCES campuses(id),

    CONSTRAINT fk_courses_department
        FOREIGN KEY (department_id)
        REFERENCES departments(id),

    CONSTRAINT fk_courses_program
        FOREIGN KEY (program_id)
        REFERENCES programs(id)
);