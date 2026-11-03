package com.smartexam.ai.service.impl;

import com.smartexam.ai.dto.AiPromptRequest;
import com.smartexam.ai.dto.AiPromptResponse;
import com.smartexam.ai.entity.AiPrompt;
import com.smartexam.ai.mapper.AiPromptMapper;
import com.smartexam.ai.repository.AiPromptRepository;
import com.smartexam.ai.service.AiPromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AiPromptServiceImpl implements AiPromptService {

    private final AiPromptRepository aiPromptRepository;
    private final AiPromptMapper aiPromptMapper;

    @Override
    public AiPromptResponse create(AiPromptRequest request) {

        if (aiPromptRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException(
                    "AI prompt already exists: " + request.name()
            );
        }

        AiPrompt prompt = AiPrompt.builder()
                .promptType(request.promptType())
                .name(request.name())
                .systemPrompt(request.systemPrompt())
                .description(request.description())
                .active(true)
                .build();

        return aiPromptMapper.toResponse(
                aiPromptRepository.save(prompt)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AiPromptResponse getById(Long id) {

        AiPrompt prompt = aiPromptRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "AI prompt not found: " + id
                        )
                );

        return aiPromptMapper.toResponse(prompt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AiPromptResponse> getAll() {

        return aiPromptRepository
                .findAllByActiveTrue()
                .stream()
                .map(aiPromptMapper::toResponse)
                .toList();
    }

    @Override
    public AiPromptResponse update(
            Long id,
            AiPromptRequest request) {

        AiPrompt prompt = aiPromptRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "AI prompt not found: " + id
                        )
                );

        prompt.setPromptType(request.promptType());
        prompt.setName(request.name());
        prompt.setSystemPrompt(request.systemPrompt());
        prompt.setDescription(request.description());

        return aiPromptMapper.toResponse(
                aiPromptRepository.save(prompt)
        );
    }

    @Override
    public void deactivate(Long id) {

        AiPrompt prompt = aiPromptRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "AI prompt not found: " + id
                        )
                );

        prompt.setActive(false);

        aiPromptRepository.save(prompt);
    }
}