package com.smartexam.ai.rag.controller;

import com.smartexam.ai.rag.dto.AiRagChatResponse;
import com.smartexam.ai.rag.dto.AiRagIngestResponse;
import com.smartexam.ai.rag.dto.AiRagReindexResponse;
import com.smartexam.ai.rag.service.AiRagService;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/ai/rag")
@RequiredArgsConstructor
public class AiRagController {

    private final AiRagService aiRagService;


    @PostMapping(
            value = "/documents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AiRagIngestResponse> ingestDocument(

            @RequestPart("file")
            MultipartFile file,

            @RequestParam(
                    value = "subjectId",
                    required = false
            )
            Long subjectId,

            @RequestParam(
                    value = "classNumber",
                    required = false
            )
            Integer classNumber) {

        return ResponseEntity.ok(
                aiRagService.ingest(
                        file,
                        subjectId,
                        classNumber
                )
        );
    }


    @GetMapping("/chat")
    public ResponseEntity<@NotNull AiRagChatResponse> chat(

            @RequestParam(value = "question")
            String question,

            @RequestParam(value = "topic", required = false)
            String topic,

            @RequestParam(value = "subjectId", required = false)
            Long subjectId,

            @RequestParam(value = "classNumber", required = false)
            Integer classNumber) {

        return ResponseEntity.ok(
                aiRagService.chat(
                        question,
                        topic,
                        subjectId,
                        classNumber
                )
        );
    }


    @PostMapping(
            "/documents/{documentId}/reindex"
    )
    public ResponseEntity<AiRagReindexResponse> reindex(

            @PathVariable("documentId")
            Long documentId) {

        return ResponseEntity.ok(
                aiRagService.reindex(
                        documentId
                )
        );
    }
}