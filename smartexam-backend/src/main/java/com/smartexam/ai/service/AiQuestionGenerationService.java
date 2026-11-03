package com.smartexam.ai.service;

import com.smartexam.ai.dto.AiQuestionGenerationRequest;
import com.smartexam.ai.dto.AiQuestionGenerationResponse;

public interface AiQuestionGenerationService {

    AiQuestionGenerationResponse generate(
            AiQuestionGenerationRequest request);
}