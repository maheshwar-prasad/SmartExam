package com.smartexam.ai.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AiQuestionGenerationRequest(

        @NotNull(message = "Class number is required")
        Integer classNumber,

        @NotNull(message = "Subject ID is required")
        Long subjectId,

        @NotBlank(message = "Topic is required")
        String topic,

        @NotBlank(message = "Difficulty is required")
        String difficulty,

        @NotNull(message = "Question count is required")
        @Min(value = 1, message = "Question count must be at least 1")
        Integer count,

        Boolean save,

         String sourceContent
) {
}