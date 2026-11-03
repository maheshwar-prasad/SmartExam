package com.smartexam.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AiAnswerEvaluationRequest(

        @NotNull(message = "Question ID is required")
        Long questionId,

        @NotBlank(message = "Student answer is required")
        String studentAnswer
) {
}