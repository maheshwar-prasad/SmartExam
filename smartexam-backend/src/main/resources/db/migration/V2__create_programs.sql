CREATE TABLE programs (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    department_id BIGINT NOT NULL,

    name VARCHAR(150) NOT NULL,

    code VARCHAR(30) NOT NULL,

    program_type VARCHAR(30) NOT NULL,

    duration INT NOT NULL,

    duration_unit VARCHAR(20) NOT NULL,

    description TEXT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NULL,

    updated_at TIMESTAMP NULL,

    created_by VARCHAR(100) NULL,

    updated_by VARCHAR(100) NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_program_organization_code
        UNIQUE (
            organization_id,
            code
        ),

    CONSTRAINT fk_program_organization
        FOREIGN KEY (
            organization_id
        )
        REFERENCES organizations(id),

    CONSTRAINT fk_program_department
        FOREIGN KEY (
            department_id
        )
        REFERENCES departments(id)
);

CREATE INDEX idx_program_organization
    ON programs(organization_id);

CREATE INDEX idx_program_department
    ON programs(department_id);