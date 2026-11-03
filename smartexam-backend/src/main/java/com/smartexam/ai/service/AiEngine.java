package com.smartexam.ai.service;

import com.smartexam.ai.enums.AiPromptType;

public interface AiEngine {

    String generate(
            AiPromptType promptType,
            String userPrompt
    );

    <T> T generateStructured(
            AiPromptType promptType,
            String userPrompt,
            Class<T> responseType
    );
}