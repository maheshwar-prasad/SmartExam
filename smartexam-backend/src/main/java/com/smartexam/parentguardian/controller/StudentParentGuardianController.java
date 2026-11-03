package com.smartexam.parentguardian.controller;

import com.smartexam.parentguardian.dto.StudentParentGuardianRequest;
import com.smartexam.parentguardian.dto.StudentParentGuardianResponse;
import com.smartexam.parentguardian.service.StudentParentGuardianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-parent-guardians")
@RequiredArgsConstructor
public class StudentParentGuardianController {

    private final StudentParentGuardianService
            studentParentGuardianService;

    // =========================================================
    // Create Relationship
    // =========================================================

    @PostMapping
    public ResponseEntity<StudentParentGuardianResponse> create(
            @Valid @RequestBody
            StudentParentGuardianRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        studentParentGuardianService.create(request)
                );
    }

    // =========================================================
    // Update Relationship
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<StudentParentGuardianResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody
            StudentParentGuardianRequest request) {

        return ResponseEntity.ok(
                studentParentGuardianService.update(
                        id,
                        request
                )
        );
    }

    // =========================================================
    // Get Relationship By ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<StudentParentGuardianResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentParentGuardianService.getById(id)
        );
    }

    // =========================================================
    // Get Parents / Guardians Of Student
    // =========================================================

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentParentGuardianResponse>>
    getByStudentId(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                studentParentGuardianService
                        .getByStudentId(studentId)
        );
    }

    // =========================================================
    // Deactivate
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id) {

        studentParentGuardianService.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // Activate
    // =========================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @PathVariable Long id) {

        studentParentGuardianService.activate(id);

        return ResponseEntity.noContent().build();
    }
}