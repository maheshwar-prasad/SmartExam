package com.smartexam.ai.dto;

import java.util.List;

public record AiExplanationResult(

        String concept,

        String explanation,

        String correctAnswerExplanation,

        String studentAnswerExplanation,

        List<String> keyPoints,

        List<String> commonMistakes
) {
}