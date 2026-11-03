package com.smartexam.ai.dto;

import java.util.List;

public record AiExplanationResponse(

        Long questionId,

        String questionText,

        String concept,

        String explanation,

        String correctAnswerExplanation,

        String studentAnswerExplanation,

        List<String> keyPoints,

        List<String> commonMistakes
) {
}