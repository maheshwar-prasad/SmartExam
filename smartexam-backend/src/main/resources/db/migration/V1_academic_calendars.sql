CREATE TABLE academic_calendars (
    id BIGINT NOT NULL AUTO_INCREMENT,
    organization_id BIGINT NOT NULL,
    academic_session_id BIGINT NOT NULL,

    title VARCHAR(200) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,

    description VARCHAR(1000) NULL,
    location VARCHAR(200) NULL,

    all_day BOOLEAN NOT NULL DEFAULT TRUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME(6) NOT NULL,
    created_by BIGINT NULL,
    updated_at DATETIME(6) NULL,
    updated_by BIGINT NULL,

    CONSTRAINT pk_academic_calendars
        PRIMARY KEY (id),

    CONSTRAINT fk_academic_calendar_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT fk_academic_calendar_session
        FOREIGN KEY (academic_session_id)
        REFERENCES academic_sessions(id),

    CONSTRAINT chk_academic_calendar_dates
        CHECK (end_date >= start_date)
);

CREATE INDEX idx_academic_calendar_organization
    ON academic_calendars(organization_id);

CREATE INDEX idx_academic_calendar_session
    ON academic_calendars(academic_session_id);

CREATE INDEX idx_academic_calendar_dates
    ON academic_calendars(start_date, end_date);

CREATE INDEX idx_academic_calendar_type
    ON academic_calendars(event_type);

CREATE INDEX idx_academic_calendar_active
    ON academic_calendars(active);