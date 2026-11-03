CREATE TABLE alumni (
    id BIGINT NOT NULL AUTO_INCREMENT,

    student_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,

    leaving_date DATE,
    graduation_year INT,

    last_class_name VARCHAR(100),
    last_academic_session VARCHAR(100),

    higher_education VARCHAR(255),
    current_organization VARCHAR(255),
    current_designation VARCHAR(150),

    current_city VARCHAR(100),
    current_country VARCHAR(100),

    remarks VARCHAR(500),

    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(255),

    PRIMARY KEY (id),

    CONSTRAINT uk_alumni_student
        UNIQUE (student_id),

    CONSTRAINT fk_alumni_student
        FOREIGN KEY (student_id)
        REFERENCES students(id),

    INDEX idx_alumni_student (student_id),
    INDEX idx_alumni_organization (organization_id),
    INDEX idx_alumni_status (status),
    INDEX idx_alumni_active (active)
);