CREATE TABLE academic_structures (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,
    academic_session_id BIGINT NOT NULL,
    class_room_id BIGINT NOT NULL,
    section_id BIGINT NOT NULL,

    name VARCHAR(200) NOT NULL,
    code VARCHAR(100) NOT NULL,
    description VARCHAR(1000) NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME(6) NOT NULL,
    created_by BIGINT NULL,
    updated_at DATETIME(6) NULL,
    updated_by BIGINT NULL,

    CONSTRAINT pk_academic_structures
        PRIMARY KEY (id),

    CONSTRAINT fk_academic_structure_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT fk_academic_structure_session
        FOREIGN KEY (academic_session_id)
        REFERENCES academic_sessions(id),

    CONSTRAINT fk_academic_structure_class
        FOREIGN KEY (class_room_id)
        REFERENCES class_rooms(id),

    CONSTRAINT fk_academic_structure_section
        FOREIGN KEY (section_id)
        REFERENCES sections(id),

    CONSTRAINT uk_academic_structure_code
        UNIQUE (organization_id, academic_session_id, code)
);

CREATE INDEX idx_academic_structure_organization
    ON academic_structures(organization_id);

CREATE INDEX idx_academic_structure_session
    ON academic_structures(academic_session_id);

CREATE INDEX idx_academic_structure_class
    ON academic_structures(class_room_id);

CREATE INDEX idx_academic_structure_section
    ON academic_structures(section_id);

CREATE INDEX idx_academic_structure_active
    ON academic_structures(active);