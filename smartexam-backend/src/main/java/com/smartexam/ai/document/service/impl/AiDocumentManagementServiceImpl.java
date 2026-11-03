package com.smartexam.ai.document.service.impl;

import com.smartexam.ai.document.dto.AiDocumentResponse;
import com.smartexam.ai.document.entity.AiDocument;
import com.smartexam.ai.document.entity.AiDocumentStatus;
import com.smartexam.ai.document.repository.AiDocumentRepository;
import com.smartexam.ai.document.service.AiDocumentManagementService;
import com.smartexam.ai.document.service.AiDocumentStorageService;
import com.smartexam.audit.entity.AuditAction;
import com.smartexam.audit.service.AuditService;
import com.smartexam.security.service.CurrentUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiDocumentManagementServiceImpl
        implements AiDocumentManagementService {

    private final AiDocumentRepository aiDocumentRepository;

    private final CurrentUserService currentUserService;
    private final AiDocumentStorageService aiDocumentStorageService;
    private final VectorStore vectorStore;
    private final AuditService auditService;



    @Override
    public List<AiDocumentResponse> getDocuments() {

        Long organizationId =
                currentUserService.getOrganizationId();

        return aiDocumentRepository
                .findAllByOrganizationIdAndActiveTrue(organizationId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AiDocumentResponse getDocument(Long documentId) {

        Long organizationId =
                currentUserService.getOrganizationId();

        AiDocument document =
                getDocumentForOrganization(
                        documentId,
                        organizationId
                );

        return toResponse(document);
    }

    @Override
    public List<AiDocumentResponse> getDocumentsBySubject(
            Long subjectId) {

        Long organizationId =
                currentUserService.getOrganizationId();

        return aiDocumentRepository
                .findAllByOrganizationIdAndSubjectIdAndActiveTrue(
                        organizationId,
                        subjectId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<AiDocumentResponse> getDocumentsByClass(
            Integer classNumber) {

        Long organizationId =
                currentUserService.getOrganizationId();

        return aiDocumentRepository
                .findAllByOrganizationIdAndClassNumberAndActiveTrue(
                        organizationId,
                        classNumber
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void updateStatus(
            Long documentId,
            AiDocumentStatus status,
            Integer chunkCount,
            String errorMessage) {

        Long organizationId =
                currentUserService.getOrganizationId();

        AiDocument document =
                getDocumentForOrganization(
                        documentId,
                        organizationId
                );

        document.setStatus(status);

        if (chunkCount != null) {
            document.setChunkCount(chunkCount);
        }

        document.setErrorMessage(errorMessage);

        document.setIndexed(
                status == AiDocumentStatus.INDEXED
        );

        aiDocumentRepository.save(document);

        log.info(
                "AI document status updated. documentId={}, organizationId={}, status={}",
                documentId,
                organizationId,
                status
        );
        auditService.log(
                AuditAction.DOCUMENT_DELETE,
                "AiDocument",
                documentId.toString(),
                "AI document deleted",
                true
        );
    }

    @Override
    @Transactional
    public void deleteDocument(Long documentId) {

        Long organizationId =
                currentUserService.getOrganizationId();

        AiDocument document =
                getDocumentForOrganization(
                        documentId,
                        organizationId
                );

        log.info(
                "AI document deletion started. documentId={}, organizationId={}",
                documentId,
                organizationId
        );

        try {

            // ---------------------------------------------------------
            // 1. Delete Qdrant vectors
            // ---------------------------------------------------------

            String filterExpression =
                    "organizationId == "
                            + organizationId
                            + " && documentId == "
                            + documentId;

            log.info(
                    "Deleting Qdrant vectors. filter={}",
                    filterExpression
            );

            vectorStore.delete(
                    filterExpression
            );


            // ---------------------------------------------------------
            // 2. Delete physical file
            // ---------------------------------------------------------

            if (document.getFileStorageKey() != null
                    && !document.getFileStorageKey().isBlank()) {

                aiDocumentStorageService.delete(
                        document.getFileStorageKey()
                );
            }


            // ---------------------------------------------------------
            // 3. Mark MySQL record deleted
            // ---------------------------------------------------------

            document.setActive(false);

            document.setStatus(
                    AiDocumentStatus.DELETED
            );

            document.setIndexed(false);

            document.setChunkCount(0);

            document.setErrorMessage(null);

            aiDocumentRepository.save(document);


            log.info(
                    "AI document deletion completed. documentId={}, organizationId={}",
                    documentId,
                    organizationId
            );

        } catch (Exception exception) {
            auditService.logFailure(
                    AuditAction.DOCUMENT_DELETE,
                    "AiDocument",
                    documentId.toString(),
                    "AI document deletion failed",
                    exception
            );
            log.error(
                    "AI document deletion failed. documentId={}, organizationId={}",
                    documentId,
                    organizationId,
                    exception
            );

            throw new IllegalStateException(
                    "Failed to delete AI document: "
                            + getSafeErrorMessage(exception),
                    exception
            );
        }
    }
    private AiDocument getDocumentForOrganization(
            Long documentId,
            Long organizationId) {

        return aiDocumentRepository
                .findByIdAndOrganizationIdAndActiveTrue(
                        documentId,
                        organizationId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "AI document not found"
                        )
                );
    }

    private AiDocumentResponse toResponse(
            AiDocument document) {

        return new AiDocumentResponse(
                document.getId(),
                document.getFileName(),
                document.getContentType(),
                document.getFileSize(),
                document.getFileExtension(),
                document.getOrganizationId(),
                document.getSubjectId(),
                document.getClassNumber(),
                document.getChunkCount(),
                document.getVectorCollection(),
                document.getStatus(),
                document.getIndexed(),
                document.getErrorMessage(),
                document.getFileChecksum(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }
    private String getSafeErrorMessage(Exception exception) {

        String message = exception.getMessage();

        if (message == null || message.isBlank()) {
            return exception.getClass().getSimpleName();
        }

        return message.length() > 1000
                ? message.substring(0, 1000)
                : message;
    }
}