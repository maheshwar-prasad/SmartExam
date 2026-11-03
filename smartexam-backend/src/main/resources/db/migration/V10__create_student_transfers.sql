CREATE TABLE student_transfers (
    id BIGINT NOT NULL AUTO_INCREMENT,

    student_id BIGINT NOT NULL,

    source_enrollment_id BIGINT NOT NULL,
    target_enrollment_id BIGINT NULL,

    source_academic_session_id BIGINT NOT NULL,
    source_class_room_id BIGINT NOT NULL,
    source_section_id BIGINT NOT NULL,

    target_academic_session_id BIGINT NOT NULL,
    target_class_room_id BIGINT NOT NULL,
    target_section_id BIGINT NOT NULL,

    transfer_date DATE NOT NULL,

    reason VARCHAR(500),
    remarks VARCHAR(1000),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL,

    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    PRIMARY KEY (id),

    CONSTRAINT fk_student_transfer_student
        FOREIGN KEY (student_id)
        REFERENCES students(id),

    CONSTRAINT fk_student_transfer_source_enrollment
        FOREIGN KEY (source_enrollment_id)
        REFERENCES student_enrollments(id),

    CONSTRAINT fk_student_transfer_target_enrollment
        FOREIGN KEY (target_enrollment_id)
        REFERENCES student_enrollments(id)
);

CREATE INDEX idx_student_transfer_student
    ON student_transfers(student_id);

CREATE INDEX idx_student_transfer_date
    ON student_transfers(transfer_date);

CREATE INDEX idx_student_transfer_source
    ON student_transfers(source_enrollment_id);

CREATE INDEX idx_student_transfer_target
    ON student_transfers(target_enrollment_id);