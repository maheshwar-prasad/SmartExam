package com.smartexam.academic.controller;

import com.smartexam.academic.dto.AcademicSessionRequest;
import com.smartexam.academic.dto.AcademicSessionResponse;
import com.smartexam.academic.dto.AcademicSessionSearchRequest;
import com.smartexam.academic.dto.AcademicSessionUpdateRequest;
import com.smartexam.academic.service.AcademicSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic-sessions")
@RequiredArgsConstructor
@Tag(
        name = "Academic Session",
        description = "Academic Session Management APIs"
)
public class AcademicSessionController {

    private final AcademicSessionService academicSessionService;

    @PostMapping
    @Operation(summary = "Create Academic Session")
    public ResponseEntity<AcademicSessionResponse> create(

            @Valid
            @RequestBody
            AcademicSessionRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(academicSessionService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Academic Session")
    public ResponseEntity<AcademicSessionResponse> update(

            @PathVariable
            Long id,
            @Valid
            @RequestBody
            AcademicSessionUpdateRequest request) {

        return ResponseEntity.ok(
                academicSessionService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Academic Session By Id")
    public ResponseEntity<AcademicSessionResponse> getById(

            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                academicSessionService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get All Academic Sessions")
    public ResponseEntity<List<AcademicSessionResponse>> getAll() {

        return ResponseEntity.ok(
                academicSessionService.getAll());
    }

    @GetMapping("/page")
    @Operation(summary = "Get Academic Sessions With Pagination")
    public ResponseEntity<Page<AcademicSessionResponse>> getAll(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "startDate")
            String sortBy,

            @RequestParam(defaultValue = "ASC")
            String direction) {

        return ResponseEntity.ok(
                academicSessionService.getAll(
                        page,
                        size,
                        sortBy,
                        direction));
    }

    @PostMapping("/search")
    @Operation(summary = "Search Academic Sessions")
    public ResponseEntity<Page<AcademicSessionResponse>> search(

            @RequestBody
            AcademicSessionSearchRequest request) {

        return ResponseEntity.ok(
                academicSessionService.search(request));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Academic Session")
    public ResponseEntity<AcademicSessionResponse> activate(

            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                academicSessionService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Academic Session")
    public ResponseEntity<AcademicSessionResponse> deactivate(

            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                academicSessionService.deactivate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Academic Session")
    public ResponseEntity<Void> delete(

            @PathVariable
            Long id) {

        academicSessionService.delete(id);

        return ResponseEntity.noContent().build();
    }

}