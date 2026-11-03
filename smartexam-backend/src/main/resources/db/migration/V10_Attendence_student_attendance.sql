CREATE TABLE student_attendance (
    id BIGINT NOT NULL AUTO_INCREMENT,
    organization_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    attendance_status VARCHAR(20) NOT NULL,
    remarks VARCHAR(500) NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    created_by BIGINT NULL,
    updated_at DATETIME(6) NULL,
    updated_by BIGINT NULL,

    PRIMARY KEY (id),

    CONSTRAINT chk_student_attendance_status
        CHECK (attendance_status IN (
            'PRESENT', 'ABSENT', 'LATE', 'ON_LEAVE'
        )),

    CONSTRAINT fk_student_attendance_organization
        FOREIGN KEY (organization_id) REFERENCES organizations(id),

    CONSTRAINT fk_student_attendance_student
        FOREIGN KEY (student_id) REFERENCES students(id),

    CONSTRAINT uk_student_attendance_date
        UNIQUE (organization_id, student_id, attendance_date)
);