package com.smartexam.ai.controller;

import com.smartexam.ai.dto.AiChatRequest;
import com.smartexam.ai.dto.AiChatResponse;
import com.smartexam.ai.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/chat")
    public ResponseEntity<AiChatResponse> chat(
            @Valid @RequestBody AiChatRequest request) {

        return ResponseEntity.ok(
                aiService.chat(request)
        );
    }
}