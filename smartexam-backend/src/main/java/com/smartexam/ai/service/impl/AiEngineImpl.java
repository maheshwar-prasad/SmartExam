package com.smartexam.ai.service.impl;

import com.smartexam.ai.config.AiProperties;
import com.smartexam.ai.entity.AiPrompt;
import com.smartexam.ai.enums.AiPromptType;
import com.smartexam.ai.repository.AiPromptRepository;
import com.smartexam.ai.service.AiEngine;
import com.smartexam.ai.validation.AiQuestionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiEngineImpl implements AiEngine {

    private final ChatClient chatClient;
    private final AiPromptRepository aiPromptRepository;
    private final ObjectMapper objectMapper;
    private final AiProperties aiProperties;
    private final AiQuestionValidator aiQuestionValidator;

    @Override
    public String generate(
            AiPromptType promptType,
            String userPrompt) {

        validate(promptType, userPrompt);

        AiPrompt prompt = getPrompt(promptType);

        String response = chatClient
                .prompt()
                .system(prompt.getSystemPrompt())
                .user(userPrompt)
                .call()
                .content();

        if (response == null || response.isBlank()) {
            throw new IllegalStateException(
                    "AI returned an empty response"
            );
        }

        return response;
    }

    @Override
    public <T> T generateStructured(
            AiPromptType promptType,
            String userPrompt,
            Class<T> responseType) {

        validate(promptType, userPrompt);

        if (responseType == null) {
            throw new IllegalArgumentException(
                    "AI response type is required"
            );
        }

        AiPrompt prompt = getPrompt(promptType);

        Exception lastException = null;

        for (int attempt = 1;
             attempt <= aiProperties.getMaxRetry();
             attempt++) {

            try {

                log.info(
                        "AI generation attempt {}/{}",
                        attempt,
                        aiProperties.getMaxRetry()
                );

                String response = chatClient
                        .prompt()
                        .system(prompt.getSystemPrompt())
                        .user(userPrompt)
                        .call()
                        .content();

                if (response == null || response.isBlank()) {
                    throw new IllegalStateException(
                            "AI returned an empty response"
                    );
                }

                log.debug(
                        "Raw AI response: {}",
                        response
                );

                String json = cleanJsonResponse(response);

                T parsed = objectMapper.readValue(
                        json,
                        responseType
                );

                return parsed;

            } catch (Exception e) {

                lastException = e;

                log.warn(
                        "AI generation failed on attempt {}/{}: {}",
                        attempt,
                        aiProperties.getMaxRetry(),
                        e.getMessage()
                );
            }
        }

        throw new RuntimeException(
                "AI generation failed after "
                        + aiProperties.getMaxRetry()
                        + " attempts",
                lastException
        );
    }

    private AiPrompt getPrompt(
            AiPromptType promptType) {

        return aiPromptRepository
                .findByPromptTypeAndActiveTrue(promptType)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No active AI prompt configured for type: "
                                        + promptType
                        )
                );
    }

    private void validate(
            AiPromptType promptType,
            String userPrompt) {

        if (promptType == null) {
            throw new IllegalArgumentException(
                    "AI prompt type is required"
            );
        }

        if (userPrompt == null || userPrompt.isBlank()) {
            throw new IllegalArgumentException(
                    "AI user prompt is required"
            );
        }
    }

    private String cleanJsonResponse(String response) {

        String cleaned = response.trim();

        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }

        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(
                    0,
                    cleaned.length() - 3
            );
        }

        return cleaned.trim();
    }
}