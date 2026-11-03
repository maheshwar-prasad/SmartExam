package com.smartexam.ai.dto;

import com.smartexam.ai.enums.AiPromptType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AiPromptRequest(

        @NotNull(message = "Prompt type is required")
        AiPromptType promptType,

        @NotBlank(message = "Prompt name is required")
        String name,

        @NotBlank(message = "System prompt is required")
        String systemPrompt,

        String description

) {
}