CREATE TABLE employee_attendance (
    id BIGINT NOT NULL AUTO_INCREMENT,

    employee_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,

    status VARCHAR(20) NOT NULL,
    check_in_time TIME NULL,
    check_out_time TIME NULL,
    remarks VARCHAR(500) NULL,

    created_at DATETIME(6) NOT NULL,
    created_by BIGINT NULL,
    updated_at DATETIME(6) NULL,
    updated_by BIGINT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    CONSTRAINT uk_employee_attendance_date
        UNIQUE (employee_id, attendance_date),

    CONSTRAINT fk_employee_attendance_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(id),

    CONSTRAINT fk_employee_attendance_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT chk_employee_attendance_status
        CHECK (
            status IN (
                'PRESENT',
                'ABSENT',
                'HALF_DAY',
                'ON_LEAVE',
                'HOLIDAY',
                'WEEKLY_OFF'
            )
        ),

    INDEX idx_employee_attendance_org_date
        (organization_id, attendance_date),

    INDEX idx_employee_attendance_status
        (status)
);