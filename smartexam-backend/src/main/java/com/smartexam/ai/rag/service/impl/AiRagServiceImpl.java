package com.smartexam.ai.rag.service.impl;

import com.smartexam.ai.document.entity.AiDocument;
import com.smartexam.ai.document.entity.AiDocumentStatus;
import com.smartexam.ai.document.repository.AiDocumentRepository;
import com.smartexam.ai.document.service.AiDocumentStorageService;
import com.smartexam.ai.rag.dto.AiRagChatResponse;
import com.smartexam.ai.rag.dto.AiRagIngestResponse;
import com.smartexam.ai.rag.dto.AiRagReindexResponse;
import com.smartexam.ai.rag.service.AiRagService;
import com.smartexam.ai.service.AiEngine;
import com.smartexam.ai.service.AiDocumentChunker;
import com.smartexam.ai.service.AiDocumentService;
import com.smartexam.ai.enums.AiPromptType;
import com.smartexam.audit.entity.AuditAction;
import com.smartexam.audit.service.AuditService;
import com.smartexam.security.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.Resource;


@Slf4j
@Service
@RequiredArgsConstructor
public class AiRagServiceImpl implements AiRagService {

    private static final String VECTOR_COLLECTION =
            "smartexam_documents";

    private static final int CHUNK_SIZE = 5000;

    private static final int CHUNK_OVERLAP = 300;

    private static final int MAX_RETRIEVED_CHUNKS = 5;

    private static final double SIMILARITY_THRESHOLD = 0.50;

    private final AiDocumentService aiDocumentService;

    private final AiDocumentChunker aiDocumentChunker;

    private final AiDocumentRepository aiDocumentRepository;

    private final VectorStore vectorStore;

    private final AiEngine aiEngine;

    private final CurrentUserService currentUserService;
    private final AiDocumentStorageService aiDocumentStorageService;

    private final AuditService auditService;

    // ============================================================
    // INGEST
    // ============================================================

    @Override
    @Transactional
    public AiRagIngestResponse ingest(
            MultipartFile file,
            Long subjectId,
            Integer classNumber) {

        validateFile(file);

        Long organizationId =
                currentUserService.getOrganizationId();

        String checksum = calculateChecksum(file);

        if (aiDocumentRepository
                .existsByOrganizationIdAndFileChecksumAndActiveTrue(
                        organizationId,
                        checksum)) {

            throw new IllegalArgumentException(
                    "This document has already been uploaded"
            );
        }

        AiDocument aiDocument =
                createDocumentRecord(
                        file,
                        organizationId,
                        subjectId,
                        classNumber,
                        checksum
                );

        try {

            log.info(
                    "RAG ingestion started. documentId={}, organizationId={}, fileName={}",
                    aiDocument.getId(),
                    organizationId,
                    file.getOriginalFilename()
            );
            auditService.log(
                    AuditAction.DOCUMENT_UPLOAD,
                    "AiDocument",
                    aiDocument.getId().toString(),
                    "AI document uploaded and indexed",
                    true,
                    """
                    {
                      "fileName": "%s",
                      "subjectId": %s,
                      "classNumber": %s,
                      "chunkCount": %s
                    }
                    """.formatted(
                            aiDocument.getFileName(),
                            aiDocument.getSubjectId(),
                            aiDocument.getClassNumber(),
                            aiDocument.getChunkCount()
                    )
            );
            String extractedText =
                    aiDocumentService
                            .extractText(file)
                            .extractedText();

            if (extractedText == null ||
                    extractedText.isBlank()) {

                throw new IllegalArgumentException(
                        "No text could be extracted from the document"
                );
            }

            List<String> chunks =
                    aiDocumentChunker.chunk(
                            extractedText,
                            CHUNK_SIZE,
                            CHUNK_OVERLAP
                    );

            if (chunks.isEmpty()) {

                throw new IllegalArgumentException(
                        "Document could not be divided into chunks"
                );
            }

            List<Document> vectorDocuments =
                    buildVectorDocuments(
                            chunks,
                            aiDocument,
                            organizationId,
                            subjectId,
                            classNumber
                    );

            vectorStore.add(vectorDocuments);

            aiDocument.setChunkCount(
                    vectorDocuments.size()
            );

            aiDocument.setStatus(
                    AiDocumentStatus.INDEXED
            );

            aiDocument.setIndexed(true);

            aiDocument.setVectorCollection(
                    VECTOR_COLLECTION
            );

            aiDocument.setErrorMessage(null);

            aiDocumentRepository.save(aiDocument);

            auditService.log(
                    AuditAction.DOCUMENT_UPLOAD,
                    "AI_DOCUMENT",
                    aiDocument.getId().toString(),
                    "AI document uploaded and indexed successfully.",
                    true
            );

            return new AiRagIngestResponse(
                    aiDocument.getFileName(),
                    vectorDocuments.size(),
                    "Document indexed successfully"
            );

        } catch (Exception exception) {
            auditService.logFailure(
                    AuditAction.DOCUMENT_UPLOAD,
                    "AiDocument",
                    aiDocument.getId().toString(),
                    "AI document upload/indexing failed",
                    exception
            );
            log.error(
                    "RAG ingestion failed. documentId={}",
                    aiDocument.getId(),
                    exception
            );

            aiDocument.setStatus(
                    AiDocumentStatus.FAILED
            );

            aiDocument.setIndexed(false);

            aiDocument.setErrorMessage(
                    getSafeErrorMessage(exception)
            );

            aiDocumentRepository.save(aiDocument);

            throw new IllegalStateException(
                    "Document indexing failed: "
                            + getSafeErrorMessage(exception),
                    exception
            );
        }
    }


    // ============================================================
    // CREATE MYSQL DOCUMENT
    // ============================================================

    private AiDocument createDocumentRecord(
            MultipartFile file,
            Long organizationId,
            Long subjectId,
            Integer classNumber,
            String checksum) {

        String fileName =
                file.getOriginalFilename();

        String extension =
                getExtension(fileName);

        AiDocument document =
                AiDocument.builder()

                        .organizationId(
                                organizationId
                        )

                        .fileName(
                                fileName
                        )

                        .contentType(
                                file.getContentType() != null
                                        ? file.getContentType()
                                        : "application/octet-stream"
                        )

                        .fileSize(
                                file.getSize()
                        )

                        .fileExtension(
                                extension
                        )

                        .subjectId(
                                subjectId
                        )

                        .classNumber(
                                classNumber
                        )

                        .chunkCount(0)

                        .vectorCollection(
                                VECTOR_COLLECTION
                        )

                        .status(
                                AiDocumentStatus.PROCESSING
                        )

                        .indexed(false)

                        .fileChecksum(
                                checksum
                        )

                        .build();

        return aiDocumentRepository.save(document);
    }


    // ============================================================
    // CREATE QDRANT DOCUMENTS
    // ============================================================

    private List<Document> buildVectorDocuments(
            List<String> chunks,
            AiDocument aiDocument,
            Long organizationId,
            Long subjectId,
            Integer classNumber) {

        List<Document> documents =
                new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {

            String chunk =
                    chunks.get(i);

            Map<String, Object> metadata =
                    new HashMap<>();

            metadata.put(
                    "documentId",
                    aiDocument.getId()
            );

            metadata.put(
                    "organizationId",
                    organizationId
            );

            metadata.put(
                    "fileName",
                    aiDocument.getFileName()
            );

            metadata.put(
                    "chunkNumber",
                    i + 1
            );

            metadata.put(
                    "documentType",
                    aiDocument.getFileExtension()
            );

            if (subjectId != null) {
                metadata.put(
                        "subjectId",
                        subjectId
                );
            }

            if (classNumber != null) {
                metadata.put(
                        "classNumber",
                        classNumber
                );
            }

            documents.add(
                    new Document(
                            chunk,
                            metadata
                    )
            );
        }

        return documents;
    }


    // ============================================================
    // SECURE RAG CHAT
    // ============================================================

    @Override
    public AiRagChatResponse chat(
            String question,
            String topic,
            Long subjectId,
            Integer classNumber) {

        if (question == null ||
                question.isBlank()) {

            throw new IllegalArgumentException(
                    "Question is required"
            );
        }

        Long organizationId =
                currentUserService.getOrganizationId();

        String filterExpression =
                buildSecurityFilter(
                        organizationId,
                        subjectId,
                        classNumber
                );

        log.info(
                "RAG search started. organizationId={}, subjectId={}, classNumber={}, filter={}",
                organizationId,
                subjectId,
                classNumber,
                filterExpression
        );

        List<Document> relevantDocuments;

        try {

            relevantDocuments =
                    vectorStore.similaritySearch(

                            SearchRequest.builder()

                                    .query(question)

                                    .topK(
                                            MAX_RETRIEVED_CHUNKS
                                    )

                                    .similarityThreshold(
                                            SIMILARITY_THRESHOLD
                                    )

                                    .filterExpression(
                                            filterExpression
                                    )

                                    .build()
                    );

        } catch (Exception exception) {

            log.error(
                    "Qdrant RAG search failed. organizationId={}, subjectId={}, classNumber={}",
                    organizationId,
                    subjectId,
                    classNumber,
                    exception
            );

            throw new IllegalStateException(
                    "Failed to search document knowledge base",
                    exception
            );
        }

        int retrievedChunks =
                relevantDocuments == null
                        ? 0
                        : relevantDocuments.size();

        log.info(
                "RAG search completed. retrievedChunks={}",
                retrievedChunks
        );

        if (relevantDocuments == null ||
                relevantDocuments.isEmpty()) {

            return new AiRagChatResponse(
                    question,
                    "I could not find relevant information in the indexed documents.",
                    0
            );
        }

        String context =
                buildContext(
                        relevantDocuments
                );

        String prompt =
                buildRagPrompt(
                        question,
                        topic,
                        subjectId,
                        classNumber,
                        context
                );

        String answer =
                aiEngine.generate(
                        AiPromptType.GENERAL_CHAT,
                        prompt
                );

        return new AiRagChatResponse(
                question,
                answer,
                retrievedChunks
        );
    }

    // ============================================================
    // SECURITY FILTER
    // ============================================================
    private String buildSecurityFilter(
            Long organizationId,
            Long subjectId,
            Integer classNumber) {

        StringBuilder filter =
                new StringBuilder();

        filter.append(
                "organizationId == "
        );

        filter.append(
                organizationId
        );

        if (subjectId != null) {

            filter.append(
                    " && subjectId == "
            );

            filter.append(
                    subjectId
            );
        }

        if (classNumber != null) {

            filter.append(
                    " && classNumber == "
            );

            filter.append(
                    classNumber
            );
        }

        return filter.toString();
    }

    // ============================================================
    // CONTEXT
    // ============================================================

    private String buildContext(
            List<Document> documents) {

        StringBuilder context =
                new StringBuilder();

        for (Document document : documents) {

            if (document.getText() == null ||
                    document.getText().isBlank()) {

                continue;
            }

            context.append(
                    "\n--- DOCUMENT CHUNK ---\n"
            );

            context.append(
                    document.getText()
            );
        }

        return context.toString();
    }


    // ============================================================
    // PROMPT
    // ============================================================

    private String buildRagPrompt(
            String question,
            String topic,
            Long subjectId,
            Integer classNumber,
            String context) {

        return """
            You are an educational AI assistant.

            Answer the student's question using ONLY
            the information contained in the supplied
            document context.

            Do not invent facts.

            If the answer cannot be found in the
            supplied context, say:

            "I could not find this information in
            the indexed study material."

            Student Question:
            %s

            Topic:
            %s

            Subject ID:
            %s

            Class:
            %s

            Document Context:
            ================================
            %s
            ================================

            Provide a clear, educational answer
            appropriate for the student's class.
            """
                .formatted(
                        question,
                        topic != null ? topic : "",
                        subjectId != null ? subjectId : "",
                        classNumber != null ? classNumber : "",
                        context
                );
    }


    // ============================================================
    // VALIDATION
    // ============================================================

    private void validateFile(
            MultipartFile file) {

        if (file == null ||
                file.isEmpty()) {

            throw new IllegalArgumentException(
                    "File is required"
            );
        }

        String fileName =
                file.getOriginalFilename();

        if (fileName == null ||
                fileName.isBlank()) {

            throw new IllegalArgumentException(
                    "File name is required"
            );
        }
    }


    private String getExtension(
            String fileName) {

        int index =
                fileName.lastIndexOf('.');

        if (index < 0) {
            return "";
        }

        return fileName
                .substring(index + 1)
                .toLowerCase();
    }


    // ============================================================
    // CHECKSUM
    // ============================================================

    private String calculateChecksum(
            MultipartFile file) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            file.getBytes()
                    );

            StringBuilder result =
                    new StringBuilder();

            for (byte value : hash) {

                result.append(
                        String.format(
                                "%02x",
                                value
                        )
                );
            }

            return result.toString();

        } catch (Exception exception) {

            throw new IllegalStateException(
                    "Unable to calculate file checksum",
                    exception
            );
        }
    }


    private String getSafeErrorMessage(
            Exception exception) {

        String message =
                exception.getMessage();

        if (message == null ||
                message.isBlank()) {

            return exception
                    .getClass()
                    .getSimpleName();
        }

        return message.length() > 1000
                ? message.substring(0, 1000)
                : message;
    }


    // ============================================================
    // REINDEX
    // ============================================================

    @Override
    @Transactional
    public AiRagReindexResponse reindex(
            Long documentId) {
//        auditService.log(
//                AuditAction.DOCUMENT_REINDEX,
//                "AiDocument",
//                documentId.toString(),
//                "AI document reindexed successfully",
//                true,
//                """
//                {
//                  "chunkCount": %s
//                }
//                """.formatted(
//                        vectorDocuments.size()
//                )
//        );
        Long organizationId =
                currentUserService.getOrganizationId();

        AiDocument document =
                aiDocumentRepository
                        .findByIdAndOrganizationIdAndActiveTrue(
                                documentId,
                                organizationId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "AI document not found"
                                )
                        );

        if (document.getFileStorageKey() == null ||
                document.getFileStorageKey().isBlank()) {

            throw new IllegalStateException(
                    "Document does not have a stored file. "
                            + "Please upload the document again."
            );
        }

        if (!aiDocumentStorageService.exists(
                document.getFileStorageKey())) {

            throw new IllegalStateException(
                    "Original document file is missing from storage."
            );
        }

        log.info(
                "RAG reindex started. documentId={}, organizationId={}",
                documentId,
                organizationId
        );

        try {

            document.setStatus(
                    AiDocumentStatus.PROCESSING
            );

            document.setIndexed(false);

            document.setErrorMessage(null);

            aiDocumentRepository.save(document);


            // ---------------------------------------------------------
            // 1. Load original file
            // ---------------------------------------------------------

            Resource resource =
                    aiDocumentStorageService.load(
                            document.getFileStorageKey()
                    );


            // ---------------------------------------------------------
            // 2. Extract text
            // ---------------------------------------------------------

            String extractedText =
                    extractTextFromResource(
                            resource
                    );

            if (extractedText == null ||
                    extractedText.isBlank()) {

                throw new IllegalArgumentException(
                        "No text could be extracted from the document"
                );
            }


            // ---------------------------------------------------------
            // 3. Chunk document
            // ---------------------------------------------------------

            List<String> chunks =
                    aiDocumentChunker.chunk(
                            extractedText,
                            CHUNK_SIZE,
                            CHUNK_OVERLAP
                    );

            if (chunks.isEmpty()) {

                throw new IllegalArgumentException(
                        "Document could not be divided into chunks"
                );
            }


            // ---------------------------------------------------------
            // 4. Delete old vectors
            // ---------------------------------------------------------

            String deleteFilter =
                    buildDocumentSecurityFilter(
                            organizationId,
                            documentId
                    );

            log.info(
                    "Deleting old Qdrant vectors. filter={}",
                    deleteFilter
            );

            vectorStore.delete(
                    deleteFilter
            );


            // ---------------------------------------------------------
            // 5. Create new vector documents
            // ---------------------------------------------------------

            List<Document> vectorDocuments =
                    buildVectorDocuments(
                            chunks,
                            document,
                            organizationId,
                            document.getSubjectId(),
                            document.getClassNumber()
                    );


            // ---------------------------------------------------------
            // 6. Insert new vectors
            // ---------------------------------------------------------

            vectorStore.add(
                    vectorDocuments
            );


            // ---------------------------------------------------------
            // 7. Update MySQL
            // ---------------------------------------------------------

            document.setChunkCount(
                    vectorDocuments.size()
            );

            document.setStatus(
                    AiDocumentStatus.INDEXED
            );

            document.setIndexed(true);

            document.setVectorCollection(
                    VECTOR_COLLECTION
            );

            document.setErrorMessage(null);

            aiDocumentRepository.save(document);


            log.info(
                    "RAG reindex completed. documentId={}, chunks={}",
                    documentId,
                    vectorDocuments.size()
            );

            return new AiRagReindexResponse(
                    documentId,
                    AiDocumentStatus.INDEXED.name(),
                    true,
                    vectorDocuments.size(),
                    "Document reindexed successfully"
            );

        } catch (Exception exception) {
            auditService.logFailure(
                    AuditAction.DOCUMENT_REINDEX,
                    "AiDocument",
                    documentId.toString(),
                    "AI document reindex failed",
                    exception
            );
            log.error(
                    "RAG reindex failed. documentId={}",
                    documentId,
                    exception
            );

            document.setStatus(
                    AiDocumentStatus.FAILED
            );

            document.setIndexed(false);

            document.setErrorMessage(
                    getSafeErrorMessage(exception)
            );

            aiDocumentRepository.save(document);

            throw new IllegalStateException(
                    "Document reindexing failed: "
                            + getSafeErrorMessage(exception),
                    exception
            );
        }
    }
    private String extractTextFromResource(
            Resource resource) {

        TikaDocumentReader reader =
                new TikaDocumentReader(resource);

        List<Document> documents =
                reader.read();

        return documents.stream()
                .map(Document::getText)
                .filter(text ->
                        text != null &&
                                !text.isBlank()
                )
                .reduce(
                        "",
                        (first, second) ->
                                first.isBlank()
                                        ? second
                                        : first + "\n\n" + second
                );
    }

    private String buildDocumentSecurityFilter(
            Long organizationId,
            Long documentId) {

        return "organizationId == "
                + organizationId
                + " && documentId == "
                + documentId;
    }
}