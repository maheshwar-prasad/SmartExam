package com.smartexam.ai.dto;

import java.util.List;

public record AiQuestionResponse(

        String questionText,

        List<String> options,

        String correctAnswer,

        String explanation,

        String difficulty,

        Integer marks
) {
}