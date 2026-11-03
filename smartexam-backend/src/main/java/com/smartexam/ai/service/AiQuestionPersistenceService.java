package com.smartexam.ai.service;

import com.smartexam.ai.dto.AiQuestionResponse;

import java.util.List;

public interface AiQuestionPersistenceService {

    List<Long> saveQuestions(
            List<AiQuestionResponse> questions
    );
}