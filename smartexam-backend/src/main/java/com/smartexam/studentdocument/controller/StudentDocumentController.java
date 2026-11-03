package com.smartexam.studentdocument.controller;

import com.smartexam.studentdocument.dto.StudentDocumentResponse;
import com.smartexam.studentdocument.enums.StudentDocumentType;
import com.smartexam.studentdocument.service.StudentDocumentService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student-documents")
@RequiredArgsConstructor
public class StudentDocumentController {

    private final StudentDocumentService studentDocumentService;


    // =========================================================
    // Get documents by student
    // =========================================================

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentDocumentResponse>> getByStudentId(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                studentDocumentService.getByStudentId(studentId)
        );
    }


    // =========================================================
    // Get document
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<StudentDocumentResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentDocumentService.getById(id)
        );
    }


    // =========================================================
    // Upload document
    // =========================================================

    @PostMapping(
            value = "/student/{studentId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<StudentDocumentResponse> upload(
            @PathVariable Long studentId,

            @RequestParam
            @NotNull
            StudentDocumentType documentType,

            @RequestParam
            @NotBlank
            String documentName,

            @RequestParam(required = false)
            String documentNumber,

            @RequestParam(required = false)
            String remarks,

            @RequestPart("file")
            MultipartFile file) {

        StudentDocumentResponse response =
                studentDocumentService.upload(
                        studentId,
                        documentType,
                        documentName,
                        documentNumber,
                        remarks,
                        file
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // Deactivate
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id) {

        studentDocumentService.deactivate(id);

        return ResponseEntity.noContent().build();
    }


    // =========================================================
    // Activate
    // =========================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @PathVariable Long id) {

        studentDocumentService.activate(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<org.springframework.core.io.Resource> download(
            @PathVariable Long id) {

        StudentDocumentResponse document =
                studentDocumentService.getById(id);

        org.springframework.core.io.Resource resource =
                studentDocumentService.download(id);

        MediaType mediaType =
                MediaType.APPLICATION_OCTET_STREAM;

        try {

            if (document.getContentType() != null) {

                mediaType =
                        MediaType.parseMediaType(
                                document.getContentType()
                        );
            }

        } catch (Exception ignored) {
            // Fall back to application/octet-stream
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                document.getOriginalFileName() +
                                "\""
                )
                .body(resource);
    }
}