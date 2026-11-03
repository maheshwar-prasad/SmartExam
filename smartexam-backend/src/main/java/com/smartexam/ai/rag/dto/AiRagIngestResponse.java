package com.smartexam.ai.rag.dto;

public record AiRagIngestResponse(
        String fileName,
        int chunkCount,
        String message
) {
}