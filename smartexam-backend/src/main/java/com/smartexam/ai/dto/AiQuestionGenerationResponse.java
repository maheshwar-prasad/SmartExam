package com.smartexam.ai.dto;

import java.util.List;

public record AiQuestionGenerationResponse(
        List<AiQuestionResponse> questions,
        boolean saved,
        List<Long> savedQuestionIds
) {
}