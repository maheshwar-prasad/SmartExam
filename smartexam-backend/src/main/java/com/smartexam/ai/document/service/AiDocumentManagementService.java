package com.smartexam.ai.document.service;

import com.smartexam.ai.document.dto.AiDocumentResponse;
import com.smartexam.ai.document.entity.AiDocumentStatus;

import java.util.List;

public interface AiDocumentManagementService {

    List<AiDocumentResponse> getDocuments();

    AiDocumentResponse getDocument(Long documentId);

    List<AiDocumentResponse> getDocumentsBySubject(Long subjectId);

    List<AiDocumentResponse> getDocumentsByClass(Integer classNumber);

    void updateStatus(
            Long documentId,
            AiDocumentStatus status,
            Integer chunkCount,
            String errorMessage
    );

    void deleteDocument(Long documentId);
}