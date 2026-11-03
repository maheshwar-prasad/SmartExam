package com.smartexam.ai.dto;

import jakarta.validation.constraints.NotBlank;

public record AiSummaryRequest(

        @NotBlank(message = "Content is required")
        String content,

        String topic,

        Integer classNumber
) {
}