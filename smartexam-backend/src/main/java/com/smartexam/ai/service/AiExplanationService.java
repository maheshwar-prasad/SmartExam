package com.smartexam.ai.service;

import com.smartexam.ai.dto.AiExplanationRequest;
import com.smartexam.ai.dto.AiExplanationResponse;

public interface AiExplanationService {

    AiExplanationResponse explain(
            AiExplanationRequest request
    );
}