-- ============================================================
-- Parent / Guardian
-- ============================================================

CREATE TABLE parent_guardians (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),

    mobile VARCHAR(20),
    alternate_mobile VARCHAR(20),
    email VARCHAR(150),

    occupation VARCHAR(150),

    address VARCHAR(500),
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(10),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    PRIMARY KEY (id),

    CONSTRAINT fk_parent_guardian_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id)
);

CREATE INDEX idx_parent_guardian_organization
    ON parent_guardians(organization_id);

CREATE INDEX idx_parent_guardian_mobile
    ON parent_guardians(mobile);

CREATE INDEX idx_parent_guardian_email
    ON parent_guardians(email);

CREATE INDEX idx_parent_guardian_active
    ON parent_guardians(active);


-- ============================================================
-- Student ↔ Parent / Guardian relationship
-- ============================================================

CREATE TABLE student_parent_guardians (
    id BIGINT NOT NULL AUTO_INCREMENT,

    student_id BIGINT NOT NULL,
    parent_guardian_id BIGINT NOT NULL,

    relation VARCHAR(30) NOT NULL,

    primary_contact BOOLEAN NOT NULL DEFAULT FALSE,
    emergency_contact BOOLEAN NOT NULL DEFAULT FALSE,

    remarks VARCHAR(500),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    PRIMARY KEY (id),

    CONSTRAINT fk_spg_student
        FOREIGN KEY (student_id)
        REFERENCES students(id),

    CONSTRAINT fk_spg_parent_guardian
        FOREIGN KEY (parent_guardian_id)
        REFERENCES parent_guardians(id),

    CONSTRAINT uk_student_parent_guardian_relation
        UNIQUE (
            student_id,
            parent_guardian_id,
            relation
        )
);

CREATE INDEX idx_spg_student
    ON student_parent_guardians(student_id);

CREATE INDEX idx_spg_parent_guardian
    ON student_parent_guardians(parent_guardian_id);

CREATE INDEX idx_spg_relation
    ON student_parent_guardians(relation);

CREATE INDEX idx_spg_active
    ON student_parent_guardians(active);