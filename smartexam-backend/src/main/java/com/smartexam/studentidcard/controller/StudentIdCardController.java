package com.smartexam.studentidcard.controller;

import com.smartexam.studentidcard.dto.StudentIdCardBulkRequest;
import com.smartexam.studentidcard.dto.StudentIdCardBulkResponse;
import com.smartexam.studentidcard.dto.StudentIdCardGenerateRequest;
import com.smartexam.studentidcard.dto.StudentIdCardResponse;
import com.smartexam.studentidcard.service.StudentIdCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-id-cards")
@RequiredArgsConstructor
@Tag(
        name = "Student ID Card",
        description = "Student IDCard APIs"
)
public class StudentIdCardController {

    private final StudentIdCardService studentIdCardService;


    @PostMapping(
            "/student/{studentId}/generate"
    )
    @Operation(summary = "Generate Student IDCard")
    public ResponseEntity<@NotNull StudentIdCardResponse> generate(
            @PathVariable Long studentId,

            @Valid
            @RequestBody
            StudentIdCardGenerateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        studentIdCardService.generate(
                                studentId,
                                request
                        )
                );
    }


    @GetMapping(
            "/student/{studentId}"
    )
    @Operation(summary = "Get Student IDCard by Student ID")
    public ResponseEntity<@NotNull List<StudentIdCardResponse>>
    getByStudentId(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                studentIdCardService
                        .getByStudentId(studentId)
        );
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get Student IDCard by ID")
    public ResponseEntity<@NotNull StudentIdCardResponse>
    getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentIdCardService.getById(id)
        );
    }


    @PostMapping("/bulk/generate")
    @Operation(summary = "Generate Bulk Student IDCard")
    public ResponseEntity<StudentIdCardBulkResponse> generateBulk(
            @Valid
            @RequestBody
            StudentIdCardBulkRequest request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        studentIdCardService
                                .generateBulk(request)
                );
    }


    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Student IDCard")
    public ResponseEntity<@NotNull Void> activate(
            @PathVariable Long id) {

        studentIdCardService.activate(id);

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Student IDCard")
    public ResponseEntity<@NotNull Void> deactivate(
            @PathVariable Long id) {

        studentIdCardService.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Download Student IDCard")
    public ResponseEntity<Resource> download(
            @PathVariable Long id) {

        StudentIdCardResponse card =
                studentIdCardService.getById(id);

        Resource resource =
                studentIdCardService.download(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                (
                                        card.getFileName() != null
                                                ? card.getFileName()
                                                : "student-id-card.pdf"
                                ) +
                                "\""
                )
                .body(resource);
    }
}