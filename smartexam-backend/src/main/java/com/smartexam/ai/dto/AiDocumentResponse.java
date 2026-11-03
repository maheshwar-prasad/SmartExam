package com.smartexam.ai.dto;

public record AiDocumentResponse(

        String fileName,

        String contentType,

        long size,

        int documentCount,

        int characterCount,

        String extractedText
) {
}