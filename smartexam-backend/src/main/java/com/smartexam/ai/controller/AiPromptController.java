package com.smartexam.ai.controller;

import com.smartexam.ai.dto.AiPromptRequest;
import com.smartexam.ai.dto.AiPromptResponse;
import com.smartexam.ai.service.AiPromptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai/prompts")
@RequiredArgsConstructor
public class AiPromptController {

    private final AiPromptService aiPromptService;

    @PostMapping
    public ResponseEntity<AiPromptResponse> create(
            @Valid @RequestBody AiPromptRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(aiPromptService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiPromptResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                aiPromptService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<AiPromptResponse>> getAll() {

        return ResponseEntity.ok(
                aiPromptService.getAll()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AiPromptResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AiPromptRequest request) {

        return ResponseEntity.ok(
                aiPromptService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id) {

        aiPromptService.deactivate(id);

        return ResponseEntity.noContent().build();
    }
}