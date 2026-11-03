ALTER TABLE organizations
    ADD COLUMN logo_path VARCHAR(500) NULL,
    ADD COLUMN logo_content_type VARCHAR(100) NULL,
    ADD COLUMN logo_original_file_name VARCHAR(255) NULL;