CREATE TABLE academic_years (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    name VARCHAR(50) NOT NULL,
    code VARCHAR(50) NOT NULL,

    start_date DATE NOT NULL,
    end_date DATE NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,
    current_year BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(100) NULL,
    updated_by VARCHAR(100) NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_academic_year_org_code
        UNIQUE (organization_id, code),

    CONSTRAINT fk_academic_year_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT chk_academic_year_dates
        CHECK (end_date > start_date)
);