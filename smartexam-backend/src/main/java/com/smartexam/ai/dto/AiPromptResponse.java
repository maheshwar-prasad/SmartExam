package com.smartexam.ai.dto;

import com.smartexam.ai.enums.AiPromptType;

public record AiPromptResponse(

        Long id,

        AiPromptType promptType,

        String name,

        String systemPrompt,

        String description,

        Boolean active

) {
}