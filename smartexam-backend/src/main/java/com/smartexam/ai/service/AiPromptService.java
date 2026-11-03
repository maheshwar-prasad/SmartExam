package com.smartexam.ai.service;

import com.smartexam.ai.dto.AiPromptRequest;
import com.smartexam.ai.dto.AiPromptResponse;

import java.util.List;

public interface AiPromptService {

    AiPromptResponse create(AiPromptRequest request);

    AiPromptResponse getById(Long id);

    List<AiPromptResponse> getAll();

    AiPromptResponse update(Long id, AiPromptRequest request);

    void deactivate(Long id);

}