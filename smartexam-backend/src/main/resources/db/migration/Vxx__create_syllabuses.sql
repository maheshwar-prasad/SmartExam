CREATE TABLE syllabuses (

    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    curriculum_id BIGINT NOT NULL,

    class_subject_id BIGINT NOT NULL,

    title VARCHAR(200) NOT NULL,

    chapter_name VARCHAR(200) NOT NULL,

    topic_name VARCHAR(300) NULL,

    description VARCHAR(1000) NULL,

    sequence_no INT NOT NULL DEFAULT 1,

    estimated_hours DECIMAL(5,2) NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME(6) NOT NULL,

    created_by BIGINT NULL,

    updated_at DATETIME(6) NULL,

    updated_by BIGINT NULL,

    CONSTRAINT pk_syllabuses
        PRIMARY KEY (id),

    CONSTRAINT fk_syllabus_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT fk_syllabus_curriculum
        FOREIGN KEY (curriculum_id)
        REFERENCES curriculums(id),

    CONSTRAINT fk_syllabus_class_subject
        FOREIGN KEY (class_subject_id)
        REFERENCES class_subjects(id)

);


CREATE INDEX idx_syllabus_organization
    ON syllabuses(organization_id);


CREATE INDEX idx_syllabus_curriculum
    ON syllabuses(curriculum_id);


CREATE INDEX idx_syllabus_class_subject
    ON syllabuses(class_subject_id);


CREATE INDEX idx_syllabus_active
    ON syllabuses(active);


CREATE INDEX idx_syllabus_sequence
    ON syllabuses(sequence_no);