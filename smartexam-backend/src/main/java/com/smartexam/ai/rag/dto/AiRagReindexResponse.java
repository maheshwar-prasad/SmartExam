package com.smartexam.ai.rag.dto;

public record AiRagReindexResponse(
        Long documentId,
        String status,
        boolean indexed,
        int chunkCount,
        String message
) {
}