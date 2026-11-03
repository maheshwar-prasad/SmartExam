package com.smartexam.ai.controller;

import com.smartexam.ai.dto.AiSummaryRequest;
import com.smartexam.ai.dto.AiSummaryResponse;
import com.smartexam.ai.service.AiSummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai/summary")
@RequiredArgsConstructor
public class AiSummaryController {

    private final AiSummaryService aiSummaryService;

    @PostMapping
    public ResponseEntity<@NotNull AiSummaryResponse> summarize(
            @Valid @RequestBody
            AiSummaryRequest request) {

        return ResponseEntity.ok(
                aiSummaryService.summarize(request)
        );
    }
}