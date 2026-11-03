package com.smartexam.ai.service;

import com.smartexam.ai.dto.AiSummaryRequest;
import com.smartexam.ai.dto.AiSummaryResponse;

public interface AiSummaryService {

    AiSummaryResponse summarize(
            AiSummaryRequest request
    );
}