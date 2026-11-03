package com.smartexam.ai.document.dto;

import com.smartexam.ai.document.entity.AiDocumentStatus;

public record AiDocumentStatusResponse(

        Long documentId,

        AiDocumentStatus status,

        Boolean indexed,

        Integer chunkCount,

        String message

) {
}