package com.smartexam.ai.controller;

import com.smartexam.ai.dto.AiExplanationRequest;
import com.smartexam.ai.dto.AiExplanationResponse;
import com.smartexam.ai.service.AiExplanationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai/explanation")
@RequiredArgsConstructor
public class AiExplanationController {

    private final AiExplanationService aiExplanationService;

    @PostMapping
    public ResponseEntity<@NotNull AiExplanationResponse> explain(
            @Valid @RequestBody
            AiExplanationRequest request) {

        return ResponseEntity.ok(
                aiExplanationService.explain(request)
        );
    }
}