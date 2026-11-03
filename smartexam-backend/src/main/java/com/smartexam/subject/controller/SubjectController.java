package com.smartexam.subject.controller;

import com.smartexam.subject.dto.SubjectRequest;
import com.smartexam.subject.dto.SubjectResponse;
import com.smartexam.subject.dto.SubjectSearchRequest;
import com.smartexam.subject.dto.SubjectUpdateRequest;
import com.smartexam.subject.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
@Validated
@Tag(name = "Subject", description = "Subject Management APIs")
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    @Operation(summary = "Create Subject")
    public ResponseEntity<SubjectResponse> create(
            @Valid @RequestBody SubjectRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subjectService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Subject")
    public ResponseEntity<SubjectResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SubjectUpdateRequest request) {

        return ResponseEntity.ok(
                subjectService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Subject By Id")
    public ResponseEntity<SubjectResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                subjectService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get All Subjects")
    public ResponseEntity<List<SubjectResponse>> getAll() {

        return ResponseEntity.ok(
                subjectService.getAll());
    }

    @GetMapping("/page")
    @Operation(summary = "Get Subjects With Pagination")
    public ResponseEntity<Page<SubjectResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        return ResponseEntity.ok(
                subjectService.getAll(
                        page,
                        size,
                        sortBy,
                        direction));
    }

    @PostMapping("/search")
    @Operation(summary = "Search Subjects")
    public ResponseEntity<Page<SubjectResponse>> search(
            @RequestBody SubjectSearchRequest request) {

        return ResponseEntity.ok(
                subjectService.search(request));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Subject")
    public ResponseEntity<SubjectResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                subjectService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Subject")
    public ResponseEntity<SubjectResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                subjectService.deactivate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Subject")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        subjectService.delete(id);

        return ResponseEntity.noContent().build();
    }

}