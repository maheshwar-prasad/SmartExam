package com.smartexam.ai.document.repository;

import com.smartexam.ai.document.entity.AiDocument;
import com.smartexam.ai.document.entity.AiDocumentStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AiDocumentRepository
        extends JpaRepository<AiDocument, Long> {

    List<AiDocument> findAllByOrganizationIdAndActiveTrue(
            Long organizationId);

    Optional<AiDocument>
    findByIdAndOrganizationIdAndActiveTrue(
            Long id,
            Long organizationId);

    List<AiDocument>
    findAllByOrganizationIdAndSubjectIdAndActiveTrue(
            Long organizationId,
            Long subjectId);

    List<AiDocument>
    findAllByOrganizationIdAndClassNumberAndActiveTrue(
            Long organizationId,
            Integer classNumber);

    List<AiDocument>
    findAllByOrganizationIdAndStatus(
            Long organizationId,
            AiDocumentStatus status);

    boolean existsByOrganizationIdAndFileChecksumAndActiveTrue(
            Long organizationId,
            String fileChecksum);
}