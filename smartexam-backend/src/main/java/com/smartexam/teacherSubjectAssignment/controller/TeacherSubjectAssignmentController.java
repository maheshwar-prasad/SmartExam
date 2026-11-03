package com.smartexam.teacherSubjectAssignment.controller;

import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentRequest;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentResponse;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentSearchRequest;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentUpdateRequest;
import com.smartexam.teacherSubjectAssignment.service.TeacherSubjectAssignmentService;
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
@RequestMapping("/api/v1/teacher-subject-assignments")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Teacher Subject Assignment",
        description = "Teacher Subject Assignment APIs"
)
public class TeacherSubjectAssignmentController {

    private final TeacherSubjectAssignmentService service;

    @PostMapping
    @Operation(summary = "Create Teacher Subject Assignment")
    public ResponseEntity<TeacherSubjectAssignmentResponse> create(
            @Valid @RequestBody TeacherSubjectAssignmentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Teacher Subject Assignment")
    public ResponseEntity<TeacherSubjectAssignmentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TeacherSubjectAssignmentUpdateRequest request) {

        return ResponseEntity.ok(
                service.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Assignment By Id")
    public ResponseEntity<TeacherSubjectAssignmentResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get All Assignments")
    public ResponseEntity<List<TeacherSubjectAssignmentResponse>> getAll() {

        return ResponseEntity.ok(
                service.getAll());
    }

    @GetMapping("/page")
    @Operation(summary = "Get Assignments With Pagination")
    public ResponseEntity<Page<TeacherSubjectAssignmentResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        return ResponseEntity.ok(
                service.getAll(page, size, sortBy, direction));
    }

    @PostMapping("/search")
    @Operation(summary = "Search Assignments")
    public ResponseEntity<Page<TeacherSubjectAssignmentResponse>> search(
            @RequestBody TeacherSubjectAssignmentSearchRequest request) {

        return ResponseEntity.ok(
                service.search(request));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Assignment")
    public ResponseEntity<TeacherSubjectAssignmentResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Assignment")
    public ResponseEntity<TeacherSubjectAssignmentResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deactivate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Assignment")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}