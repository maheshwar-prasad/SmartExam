CREATE TABLE notifications (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    title VARCHAR(150) NOT NULL,
    message VARCHAR(1000) NOT NULL,

    type VARCHAR(30) NOT NULL,

    `read` BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    INDEX idx_notification_user (user_id),
    INDEX idx_notification_organization (organization_id),
    INDEX idx_notification_read (`read`),
    INDEX idx_notification_active (active)
);