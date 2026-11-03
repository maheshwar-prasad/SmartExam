package com.smartexam.ai.controller;

import com.smartexam.ai.dto.AiQuestionGenerationRequest;
import com.smartexam.ai.dto.AiQuestionGenerationResponse;
import com.smartexam.ai.service.AiQuestionGenerationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai/questions")
@RequiredArgsConstructor
public class AiQuestionController {

    private final AiQuestionGenerationService
            aiQuestionGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<@NotNull AiQuestionGenerationResponse> generate(
            @Valid @RequestBody
            AiQuestionGenerationRequest request) {

        return ResponseEntity.ok(
                aiQuestionGenerationService.generate(request)
        );
    }
}