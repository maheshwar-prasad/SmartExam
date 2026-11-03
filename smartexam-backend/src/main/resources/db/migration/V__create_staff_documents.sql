CREATE TABLE staff_document_types (
    id BIGINT NOT NULL AUTO_INCREMENT,
    organization_id BIGINT NOT NULL,

    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL,
    description VARCHAR(500),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME(6) NOT NULL,
    created_by BIGINT,
    updated_at DATETIME(6),
    updated_by BIGINT,

    PRIMARY KEY (id),

    CONSTRAINT uk_staff_document_type_code_org
        UNIQUE (organization_id, code),

    CONSTRAINT fk_staff_document_type_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    INDEX idx_staff_document_type_org (organization_id),
    INDEX idx_staff_document_type_active (active)
);


CREATE TABLE staff_documents (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,
    teacher_id BIGINT NOT NULL,
    document_type_id BIGINT NOT NULL,

    document_number VARCHAR(100),
    document_name VARCHAR(255) NOT NULL,

    issue_date DATE,
    expiry_date DATE,

    description VARCHAR(1000),

    file_name VARCHAR(255),
    stored_file_name VARCHAR(255),
    file_path VARCHAR(1000),
    file_type VARCHAR(100),
    file_size BIGINT,

    verification_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    verified_by BIGINT,
    verified_at DATETIME(6),
    verification_comments VARCHAR(1000),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME(6) NOT NULL,
    created_by BIGINT,
    updated_at DATETIME(6),
    updated_by BIGINT,

    PRIMARY KEY (id),

    CONSTRAINT fk_staff_document_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT fk_staff_document_teacher
        FOREIGN KEY (teacher_id)
        REFERENCES teachers(id),

    CONSTRAINT fk_staff_document_type
        FOREIGN KEY (document_type_id)
        REFERENCES staff_document_types(id),

    INDEX idx_staff_document_org
        (organization_id),

    INDEX idx_staff_document_teacher
        (teacher_id),

    INDEX idx_staff_document_type
        (document_type_id),

    INDEX idx_staff_document_status
        (verification_status),

    INDEX idx_staff_document_expiry
        (expiry_date),

    INDEX idx_staff_document_active
        (active)
);