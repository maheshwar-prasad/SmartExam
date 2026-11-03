CREATE TABLE campuses (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    name VARCHAR(150) NOT NULL,

    code VARCHAR(50) NOT NULL,

    campus_type VARCHAR(50) NULL,

    address_line1 VARCHAR(255) NULL,

    address_line2 VARCHAR(255) NULL,

    city VARCHAR(100) NULL,

    state VARCHAR(100) NULL,

    postal_code VARCHAR(20) NULL,

    country VARCHAR(100) NULL,

    phone VARCHAR(30) NULL,

    email VARCHAR(150) NULL,

    description VARCHAR(500) NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    created_by VARCHAR(100) NULL,

    updated_by VARCHAR(100) NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_campuses_organization_code
        UNIQUE (organization_id, code),

    CONSTRAINT fk_campuses_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id)
) ENGINE=InnoDB;