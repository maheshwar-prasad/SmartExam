CREATE TABLE class_rooms (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    name VARCHAR(100) NOT NULL,

    code VARCHAR(50) NOT NULL,

    description VARCHAR(500),

    display_order INT,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    created_by BIGINT,

    updated_by BIGINT,

    PRIMARY KEY (id),

    CONSTRAINT uk_class_rooms_organization_code
        UNIQUE (organization_id, code),

    CONSTRAINT fk_class_rooms_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id)
);