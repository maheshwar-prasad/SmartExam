package com.smartexam.ai.dto;

import java.util.List;

public record AiSummaryResponse(

        String topic,

        Integer classNumber,

        String summary,

        List<String> keyPoints,

        List<String> importantTerms,

        List<String> quickRevisionPoints
) {
}