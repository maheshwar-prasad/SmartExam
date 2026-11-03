package com.smartexam.ai.service;

import com.smartexam.ai.dto.AiChatRequest;
import com.smartexam.ai.dto.AiChatResponse;
import org.springframework.ai.chat.client.ChatClient;

public interface AiService {

    AiChatResponse chat(AiChatRequest request);
}