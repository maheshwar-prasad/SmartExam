package com.smartexam.ai.dto;

import com.smartexam.ai.enums.AiPromptType;
import jakarta.validation.constraints.NotBlank;

public record AiChatRequest(

        @NotBlank(message = "Prompt is required")
        String prompt,

        AiPromptType promptType

) {
}