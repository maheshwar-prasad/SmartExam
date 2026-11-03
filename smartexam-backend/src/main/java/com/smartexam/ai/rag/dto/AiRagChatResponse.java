package com.smartexam.ai.rag.dto;

public record AiRagChatResponse(
        String question,
        String answer,
        int retrievedChunks
) {
}