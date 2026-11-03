CREATE TABLE attendance
(
    id BIGINT NOT NULL AUTO_INCREMENT,

    attendance_date DATE NOT NULL,

    student_enrollment_id BIGINT NOT NULL,

    teacher_subject_assignment_id BIGINT NOT NULL,

    status VARCHAR(20) NOT NULL,

    remarks VARCHAR(500),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    created_by BIGINT,

    updated_by BIGINT,

    CONSTRAINT pk_attendance
        PRIMARY KEY (id),

    CONSTRAINT uk_attendance
        UNIQUE (
            attendance_date,
            student_enrollment_id,
            teacher_subject_assignment_id
        ),

    CONSTRAINT fk_attendance_student
        FOREIGN KEY (student_enrollment_id)
            REFERENCES student_enrollments(id),

    CONSTRAINT fk_attendance_teacher_subject
        FOREIGN KEY (teacher_subject_assignment_id)
            REFERENCES teacher_subject_assignments(id)
);

CREATE INDEX idx_attendance_student
ON attendance(student_enrollment_id);

CREATE INDEX idx_attendance_teacher_subject
ON attendance(teacher_subject_assignment_id);

CREATE INDEX idx_attendance_date
ON attendance(attendance_date);

CREATE INDEX idx_attendance_status
ON attendance(status);

CREATE INDEX idx_attendance_active
ON attendance(active);