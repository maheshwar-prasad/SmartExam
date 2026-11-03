ALTER TABLE ai_documents
ADD COLUMN file_storage_key VARCHAR(500)
AFTER file_extension;