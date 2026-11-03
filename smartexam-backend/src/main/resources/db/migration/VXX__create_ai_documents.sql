CREATE TABLE ai_documents (
    id BIGINT NOT NULL AUTO_INCREMENT,

    active BIT(1) NOT NULL DEFAULT 1,

    created_at DATETIME(6) NOT NULL,
    created_by BIGINT,

    updated_at DATETIME(6),
    updated_by BIGINT,

    organization_id BIGINT NOT NULL,

    file_name VARCHAR(255) NOT NULL,

    content_type VARCHAR(150) NOT NULL,

    file_size BIGINT NOT NULL,

    file_extension VARCHAR(20),

    subject_id BIGINT,

    class_number INT,

    chunk_count INT NOT NULL DEFAULT 0,

    vector_collection VARCHAR(150),

    status VARCHAR(30) NOT NULL,

    error_message TEXT,

    indexed BIT(1) NOT NULL DEFAULT 0,

    file_checksum VARCHAR(128),

    PRIMARY KEY (id),

    INDEX idx_ai_document_organization (organization_id),

    INDEX idx_ai_document_subject (subject_id),

    INDEX idx_ai_document_status (status),

    INDEX idx_ai_document_created_at (created_at),

    INDEX idx_ai_document_checksum (file_checksum)
) ENGINE=InnoDB;