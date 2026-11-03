package com.smartexam.ai.service;

import com.smartexam.ai.dto.AiAnswerEvaluationRequest;
import com.smartexam.ai.dto.AiAnswerEvaluationResponse;

public interface AiAnswerEvaluationService {

    AiAnswerEvaluationResponse evaluate(
            AiAnswerEvaluationRequest request
    );
}