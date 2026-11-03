package com.smartexam.studentenrollment.controller;

import com.smartexam.studentenrollment.dto.StudentEnrollmentRequest;
import com.smartexam.studentenrollment.dto.StudentEnrollmentResponse;
import com.smartexam.studentenrollment.dto.StudentEnrollmentSearchRequest;
import com.smartexam.studentenrollment.dto.StudentEnrollmentUpdateRequest;
import com.smartexam.studentenrollment.service.StudentEnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-enrollments")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Student Enrollment",
        description = "Student Enrollment APIs"
)
public class StudentEnrollmentController {

    private final StudentEnrollmentService service;

    @PostMapping
    @Operation(summary = "Create Student Enrollment")
    public ResponseEntity<@NotNull StudentEnrollmentResponse> create(
            @Valid @RequestBody StudentEnrollmentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Student Enrollment")
    public ResponseEntity<@NotNull StudentEnrollmentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentEnrollmentUpdateRequest request) {

        return ResponseEntity.ok(
                service.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Student Enrollment By Id")
    public ResponseEntity<@NotNull StudentEnrollmentResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get All Student Enrollments")
    public ResponseEntity<@NotNull List<StudentEnrollmentResponse>> getAll() {

        return ResponseEntity.ok(
                service.getAll());
    }

    @GetMapping("/page")
    @Operation(summary = "Get Student Enrollments With Pagination")
    public ResponseEntity<@NotNull Page<@NotNull StudentEnrollmentResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        return ResponseEntity.ok(
                service.getAll(page, size, sortBy, direction));
    }

    @PostMapping("/search")
    @Operation(summary = "Search Student Enrollments")
    public ResponseEntity<@NotNull Page<@NotNull StudentEnrollmentResponse>> search(
            @RequestBody StudentEnrollmentSearchRequest request) {

        return ResponseEntity.ok(
                service.search(request));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Student Enrollment")
    public ResponseEntity<@NotNull StudentEnrollmentResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Student Enrollment")
    public ResponseEntity<@NotNull StudentEnrollmentResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deactivate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Student Enrollment")
    public ResponseEntity<@NotNull Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}