package com.smartexam.ai.document.dto;

import com.smartexam.ai.document.entity.AiDocumentStatus;

import java.time.LocalDateTime;

public record AiDocumentResponse(

        Long id,

        String fileName,

        String contentType,

        Long fileSize,

        String fileExtension,

        Long organizationId,

        Long subjectId,

        Integer classNumber,

        Integer chunkCount,

        String vectorCollection,

        AiDocumentStatus status,

        Boolean indexed,

        String errorMessage,

        String fileChecksum,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}