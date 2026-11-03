package com.smartexam.ai.dto;

import java.util.List;

public record AiAnswerEvaluationResponse(

        Long questionId,

        boolean correct,

        double score,

        double maxScore,

        String feedback,

        List<String> strengths,

        List<String> improvements,

        String evaluation
) {
}