package com.smartexam.ai.document.controller;

import com.smartexam.ai.document.dto.AiDocumentResponse;
import com.smartexam.ai.document.service.AiDocumentManagementService;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai/documents")
@RequiredArgsConstructor
public class AiDocumentManagementController {

    private final AiDocumentManagementService
            aiDocumentManagementService;

    @GetMapping
    public ResponseEntity<@NotNull List<AiDocumentResponse>> getDocuments() {

        return ResponseEntity.ok(
                aiDocumentManagementService.getDocuments()
        );
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<@NotNull AiDocumentResponse> getDocument(
            @PathVariable("documentId") Long documentId) {

        return ResponseEntity.ok(
                aiDocumentManagementService.getDocument(documentId)
        );
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<@NotNull List<AiDocumentResponse>>
    getDocumentsBySubject(
            @PathVariable("subjectId") Long subjectId) {

        return ResponseEntity.ok(
                aiDocumentManagementService
                        .getDocumentsBySubject(subjectId)
        );
    }

    @GetMapping("/class/{classNumber}")
    public ResponseEntity<@NotNull List<AiDocumentResponse>>
    getDocumentsByClass(
            @PathVariable("classNumber") Integer classNumber) {

        return ResponseEntity.ok(
                aiDocumentManagementService
                        .getDocumentsByClass(classNumber)
        );
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<@NotNull Void> deleteDocument(
            @PathVariable("documentId") Long documentId) {

        aiDocumentManagementService
                .deleteDocument(documentId);

        return ResponseEntity.noContent().build();
    }
}