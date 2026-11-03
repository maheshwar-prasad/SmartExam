package com.smartexam.ai.mapper;

import com.smartexam.ai.dto.AiPromptResponse;
import com.smartexam.ai.entity.AiPrompt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AiPromptMapper {

    AiPromptResponse toResponse(AiPrompt entity);
}