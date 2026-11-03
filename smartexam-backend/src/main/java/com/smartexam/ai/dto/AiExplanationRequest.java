package com.smartexam.ai.dto;

import jakarta.validation.constraints.NotNull;

public record AiExplanationRequest(

        @NotNull(message = "Question ID is required")
        Long questionId,

        String studentAnswer
) {
}