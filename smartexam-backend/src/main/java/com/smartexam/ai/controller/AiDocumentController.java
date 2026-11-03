package com.smartexam.ai.controller;

import com.smartexam.ai.dto.AiDocumentResponse;
import com.smartexam.ai.dto.AiQuestionGenerationResponse;
import com.smartexam.ai.dto.AiSummaryResponse;
import com.smartexam.ai.service.AiDocumentQuestionGenerationService;
import com.smartexam.ai.service.AiDocumentService;

import com.smartexam.ai.service.AiDocumentSummaryService;
import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/ai/documents")
@RequiredArgsConstructor
public class AiDocumentController {

    private final AiDocumentService aiDocumentService;
    private final AiDocumentSummaryService aiDocumentSummaryService;
    private final AiDocumentQuestionGenerationService
            aiDocumentQuestionGenerationService;

    @PostMapping(
            value = "/extract",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AiDocumentResponse> extractText(
            @RequestPart("file")
            MultipartFile file) {

        return ResponseEntity.ok(
                aiDocumentService.extractText(file)
        );
    }

    @PostMapping(
            value = "/summary",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<@NotNull AiSummaryResponse> summarizeDocument(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "topic", required = false) String topic,
            @RequestParam(value = "classNumber", required = false) Integer classNumber) {

        return ResponseEntity.ok(
                aiDocumentSummaryService.summarize(
                        file,
                        topic,
                        classNumber
                )
        );
    }

    @PostMapping(
            value = "/questions",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<@NotNull AiQuestionGenerationResponse>
    generateQuestionsFromDocument(

            @RequestPart("file")
            MultipartFile file,

            @RequestParam(value = "classNumber")
            Integer classNumber,

            @RequestParam(value = "subjectId")
            Long subjectId,

            @RequestParam(value = "topic")
            String topic,

            @RequestParam(value = "difficulty")
            String difficulty,

            @RequestParam(value = "count")
            Integer count,

            @RequestParam(value = "save", defaultValue = "false")
            Boolean save) {

        return ResponseEntity.ok(
                aiDocumentQuestionGenerationService.generate(
                        file,
                        classNumber,
                        subjectId,
                        topic,
                        difficulty,
                        count,
                        save
                )
        );
    }
}