package com.smartexam.ai.controller;

import com.smartexam.ai.dto.AiAnswerEvaluationRequest;
import com.smartexam.ai.dto.AiAnswerEvaluationResponse;
import com.smartexam.ai.service.AiAnswerEvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai/answers")
@RequiredArgsConstructor
public class AiAnswerEvaluationController {

    private final AiAnswerEvaluationService
            aiAnswerEvaluationService;

    @PostMapping("/evaluate")
    public ResponseEntity<@NotNull AiAnswerEvaluationResponse> evaluate(
            @Valid @RequestBody
            AiAnswerEvaluationRequest request) {

        return ResponseEntity.ok(
                aiAnswerEvaluationService.evaluate(request)
        );
    }
}