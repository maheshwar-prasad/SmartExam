CREATE TABLE teacher_timetables (

    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    teacher_id BIGINT NOT NULL,

    academic_session_id BIGINT NOT NULL,

    class_room_id BIGINT NOT NULL,

    section_id BIGINT NOT NULL,

    subject_id BIGINT NOT NULL,

    day_of_week VARCHAR(20) NOT NULL,

    period_number INT NOT NULL,

    start_time TIME NOT NULL,

    end_time TIME NOT NULL,

    room VARCHAR(100) NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME(6) NOT NULL,

    created_by BIGINT NULL,

    updated_at DATETIME(6) NULL,

    updated_by BIGINT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_teacher_timetable_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations (id),

    CONSTRAINT fk_teacher_timetable_teacher
        FOREIGN KEY (teacher_id)
        REFERENCES teachers (id),

    CONSTRAINT fk_teacher_timetable_academic_session
        FOREIGN KEY (academic_session_id)
        REFERENCES academic_sessions (id),

    CONSTRAINT fk_teacher_timetable_class_room
        FOREIGN KEY (class_room_id)
        REFERENCES class_rooms (id),

    CONSTRAINT fk_teacher_timetable_section
        FOREIGN KEY (section_id)
        REFERENCES sections (id),

    CONSTRAINT fk_teacher_timetable_subject
        FOREIGN KEY (subject_id)
        REFERENCES subjects (id),

    INDEX idx_teacher_timetable_org
        (organization_id),

    INDEX idx_teacher_timetable_teacher
        (teacher_id),

    INDEX idx_teacher_timetable_session
        (academic_session_id),

    INDEX idx_teacher_timetable_class
        (class_room_id),

    INDEX idx_teacher_timetable_section
        (section_id),

    INDEX idx_teacher_timetable_subject
        (subject_id),

    INDEX idx_teacher_timetable_day
        (day_of_week),

    INDEX idx_teacher_timetable_active
        (active)

);