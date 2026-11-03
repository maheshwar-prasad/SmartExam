CREATE TABLE institution_settings (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    institution_name VARCHAR(150) NOT NULL,

    institution_code VARCHAR(50) NOT NULL,

    email VARCHAR(150),

    phone VARCHAR(20),

    website VARCHAR(255),

    address VARCHAR(500),

    city VARCHAR(100),

    state VARCHAR(100),

    country VARCHAR(100),

    postal_code VARCHAR(20),

    logo_url VARCHAR(500),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT uk_institution_settings_organization
        UNIQUE (organization_id),

    CONSTRAINT uk_institution_settings_code
        UNIQUE (institution_code)
);