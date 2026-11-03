package com.smartexam.ai.dto;

import java.util.List;

public record AiQuestionsResponse(
        List<AiQuestionResponse> questions
) {
}