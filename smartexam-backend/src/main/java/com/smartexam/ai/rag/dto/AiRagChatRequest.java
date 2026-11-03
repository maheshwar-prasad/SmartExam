package com.smartexam.ai.rag.dto;

import jakarta.validation.constraints.NotBlank;

public record AiRagChatRequest(

        @NotBlank(message = "Question is required")
        String question,

        String topic,

        Long subjectId,

        Integer classNumber
) {
}