-- ============================================================
-- LEAVE MANAGEMENT
-- ============================================================

-- ============================================================
-- LEAVE TYPES
-- ============================================================

CREATE TABLE leave_types (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    name VARCHAR(100) NOT NULL,

    code VARCHAR(50) NOT NULL,

    description VARCHAR(500),

    days_per_year DECIMAL(5,2) NOT NULL DEFAULT 0,

    carry_forward_allowed BOOLEAN NOT NULL DEFAULT FALSE,

    max_carry_forward_days DECIMAL(5,2),

    requires_approval BOOLEAN NOT NULL DEFAULT TRUE,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME(6) NOT NULL,

    created_by BIGINT,

    updated_at DATETIME(6),

    updated_by BIGINT,

    PRIMARY KEY (id),

    CONSTRAINT uk_leave_type_code_org
        UNIQUE (organization_id, code),

    CONSTRAINT fk_leave_type_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    INDEX idx_leave_type_org (organization_id),

    INDEX idx_leave_type_active (active)
);


-- ============================================================
-- LEAVE BALANCES
-- ============================================================

CREATE TABLE leave_balances (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    teacher_id BIGINT NOT NULL,

    leave_type_id BIGINT NOT NULL,

    academic_session_id BIGINT NOT NULL,

    allocated_days DECIMAL(5,2) NOT NULL DEFAULT 0,

    used_days DECIMAL(5,2) NOT NULL DEFAULT 0,

    pending_days DECIMAL(5,2) NOT NULL DEFAULT 0,

    remaining_days DECIMAL(5,2) NOT NULL DEFAULT 0,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME(6) NOT NULL,

    created_by BIGINT,

    updated_at DATETIME(6),

    updated_by BIGINT,

    PRIMARY KEY (id),

    CONSTRAINT uk_leave_balance
        UNIQUE (
            organization_id,
            teacher_id,
            leave_type_id,
            academic_session_id
        ),

    CONSTRAINT fk_leave_balance_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT fk_leave_balance_teacher
        FOREIGN KEY (teacher_id)
        REFERENCES teachers(id),

    CONSTRAINT fk_leave_balance_type
        FOREIGN KEY (leave_type_id)
        REFERENCES leave_types(id),

    CONSTRAINT fk_leave_balance_session
        FOREIGN KEY (academic_session_id)
        REFERENCES academic_sessions(id),

    INDEX idx_leave_balance_teacher
        (teacher_id),

    INDEX idx_leave_balance_type
        (leave_type_id),

    INDEX idx_leave_balance_session
        (academic_session_id),

    INDEX idx_leave_balance_active
        (active)
);


-- ============================================================
-- LEAVE REQUESTS
-- ============================================================

CREATE TABLE leave_requests (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,

    teacher_id BIGINT NOT NULL,

    leave_type_id BIGINT NOT NULL,

    academic_session_id BIGINT NOT NULL,

    from_date DATE NOT NULL,

    to_date DATE NOT NULL,

    number_of_days DECIMAL(5,2) NOT NULL,

    reason VARCHAR(1000) NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    applied_at DATETIME(6) NOT NULL,

    approved_by BIGINT,

    approved_at DATETIME(6),

    rejected_by BIGINT,

    rejected_at DATETIME(6),

    approval_comments VARCHAR(1000),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME(6) NOT NULL,

    created_by BIGINT,

    updated_at DATETIME(6),

    updated_by BIGINT,

    PRIMARY KEY (id),

    CONSTRAINT fk_leave_request_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT fk_leave_request_teacher
        FOREIGN KEY (teacher_id)
        REFERENCES teachers(id),

    CONSTRAINT fk_leave_request_type
        FOREIGN KEY (leave_type_id)
        REFERENCES leave_types(id),

    CONSTRAINT fk_leave_request_session
        FOREIGN KEY (academic_session_id)
        REFERENCES academic_sessions(id),

    INDEX idx_leave_request_teacher
        (teacher_id),

    INDEX idx_leave_request_type
        (leave_type_id),

    INDEX idx_leave_request_session
        (academic_session_id),

    INDEX idx_leave_request_status
        (status),

    INDEX idx_leave_request_dates
        (from_date, to_date),

    INDEX idx_leave_request_active
        (active)
);