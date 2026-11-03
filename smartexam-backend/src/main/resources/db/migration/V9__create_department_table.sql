CREATE TABLE departments (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    department_code VARCHAR(50) NOT NULL,
    department_name VARCHAR(150) NOT NULL,
    description VARCHAR(500),

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    created_by BIGINT,
    updated_by BIGINT,

    PRIMARY KEY (id),

    CONSTRAINT uk_department_org_code
        UNIQUE (organization_id, department_code),

    CONSTRAINT uk_department_org_name
        UNIQUE (organization_id, department_name),

    CONSTRAINT fk_department_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id)
);