CREATE TABLE class_subjects (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    academic_session_id BIGINT NOT NULL,

    class_room_id BIGINT NOT NULL,

    subject_id BIGINT NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME(6) NOT NULL,
    created_by BIGINT NULL,

    updated_at DATETIME(6) NULL,
    updated_by BIGINT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_class_subject
        UNIQUE (
            organization_id,
            academic_session_id,
            class_room_id,
            subject_id
        ),

    CONSTRAINT fk_class_subject_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations (id),

    CONSTRAINT fk_class_subject_academic_session
        FOREIGN KEY (academic_session_id)
        REFERENCES academic_sessions (id),

    CONSTRAINT fk_class_subject_class_room
        FOREIGN KEY (class_room_id)
        REFERENCES class_rooms (id),

    CONSTRAINT fk_class_subject_subject
        FOREIGN KEY (subject_id)
        REFERENCES subjects (id),

    INDEX idx_class_subject_organization (
        organization_id
    ),

    INDEX idx_class_subject_academic_session (
        academic_session_id
    ),

    INDEX idx_class_subject_class_room (
        class_room_id
    ),

    INDEX idx_class_subject_subject (
        subject_id
    ),

    INDEX idx_class_subject_active (
        active
    )
);