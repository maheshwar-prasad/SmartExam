package com.smartexam.ai.service.impl;

import com.smartexam.ai.dto.AiChatRequest;
import com.smartexam.ai.dto.AiChatResponse;
import com.smartexam.ai.enums.AiPromptType;
import com.smartexam.ai.repository.AiPromptRepository;
import com.smartexam.ai.service.AiEngine;
import com.smartexam.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final AiPromptRepository aiPromptRepository;
    private final AiEngine aiEngine;

    @Override
    public AiChatResponse chat(AiChatRequest request) {

        AiPromptType promptType =
                request.promptType() != null
                        ? request.promptType()
                        : AiPromptType.GENERAL_CHAT;

        String response = aiEngine.generate(
                promptType,
                request.prompt()
        );

        return new AiChatResponse(response);
    }
}