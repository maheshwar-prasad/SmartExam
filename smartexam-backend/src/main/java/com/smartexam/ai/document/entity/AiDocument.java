package com.smartexam.ai.document.entity;

import com.smartexam.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "ai_documents",
        indexes = {
                @Index(
                        name = "idx_ai_document_organization",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_ai_document_subject",
                        columnList = "subject_id"
                ),
                @Index(
                        name = "idx_ai_document_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_ai_document_created_at",
                        columnList = "created_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AiDocument extends BaseEntity {

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "content_type", nullable = false, length = 150)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_extension", length = 20)
    private String fileExtension;

    @Column(name = "file_storage_key", length = 500)
    private String fileStorageKey;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "class_number")
    private Integer classNumber;

    @Column(name = "chunk_count", nullable = false)
    private Integer chunkCount;

    @Column(name = "vector_collection", length = 150)
    private String vectorCollection;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AiDocumentStatus status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "indexed", nullable = false)
    private Boolean indexed;

    @Column(name = "file_checksum", length = 128)
    private String fileChecksum;
}