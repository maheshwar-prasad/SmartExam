CREATE TABLE grading_systems (
    id BIGINT NOT NULL AUTO_INCREMENT,
    organization_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(100) NOT NULL,
    description VARCHAR(1000) NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    created_by BIGINT NULL,
    updated_at DATETIME(6) NULL,
    updated_by BIGINT NULL,

    CONSTRAINT pk_grading_systems
        PRIMARY KEY (id),

    CONSTRAINT fk_grading_system_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT uk_grading_system_code
        UNIQUE (organization_id, code)
);

CREATE INDEX idx_grading_system_organization
    ON grading_systems(organization_id);

CREATE INDEX idx_grading_system_active
    ON grading_systems(active);