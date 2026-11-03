
CREATE TABLE departments (

    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    campus_id BIGINT NULL,

    name VARCHAR(150) NOT NULL,

    code VARCHAR(50) NOT NULL,

    description VARCHAR(500) NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    created_by VARCHAR(100) NULL,

    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),

    updated_by VARCHAR(100) NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_department_org_code
        UNIQUE (organization_id, code),

    CONSTRAINT fk_department_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT fk_department_campus
        FOREIGN KEY (campus_id)
        REFERENCES campuses(id)

);