CREATE TABLE audit_logs (

    id BIGINT NOT NULL AUTO_INCREMENT,

    active BIT(1) NOT NULL DEFAULT b'1',

    created_at DATETIME(6) NOT NULL,

    created_by BIGINT,

    updated_at DATETIME(6),

    updated_by BIGINT,

    organization_id BIGINT NOT NULL,

    user_id BIGINT,

    action VARCHAR(80) NOT NULL,

    entity_type VARCHAR(100),

    entity_id VARCHAR(100),

    description VARCHAR(1000),

    ip_address VARCHAR(100),

    user_agent VARCHAR(1000),

    request_method VARCHAR(20),

    request_uri VARCHAR(1000),

    success BIT(1) NOT NULL DEFAULT b'1',

    error_message TEXT,

    metadata JSON,

    PRIMARY KEY (id),

    INDEX idx_audit_organization (organization_id),

    INDEX idx_audit_user (user_id),

    INDEX idx_audit_action (action),

    INDEX idx_audit_entity (
        entity_type,
        entity_id
    ),

    INDEX idx_audit_created_at (created_at)

) ENGINE=InnoDB;