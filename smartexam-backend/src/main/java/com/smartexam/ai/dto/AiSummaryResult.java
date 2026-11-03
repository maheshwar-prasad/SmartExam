package com.smartexam.ai.dto;

import java.util.List;

public record AiSummaryResult(

        String summary,

        List<String> keyPoints,

        List<String> importantTerms,

        List<String> quickRevisionPoints
) {
}