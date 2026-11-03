package com.smartexam.student.controller;

import com.smartexam.common.dto.PageResponse;
import com.smartexam.student.dto.StudentRequest;
import com.smartexam.student.dto.StudentResponse;
import com.smartexam.student.dto.StudentSearchRequest;
import com.smartexam.student.dto.StudentUpdateRequest;
import com.smartexam.student.service.StudentService;
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
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Student API", description = "Student Management APIs")
public class StudentController {

    private final StudentService studentService;

    /**
     * Create Student
     */
    @PostMapping
    @Operation(summary = "Create Student")
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody StudentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.create(request));
    }

    /**
     * Update Student
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update Student")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateRequest request) {

        return ResponseEntity.ok(
                studentService.update(id, request));
    }

    /**
     * Get Student By Id
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get Student By Id")
    public ResponseEntity<StudentResponse> getStudent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentService.getById(id));
    }

    /**
     * Get All Students
     */
    @GetMapping
    @Operation(summary = "Get All Students")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {

        return ResponseEntity.ok(
                studentService.getAll());
    }

    /**
     * Student Pagination
     */
    @GetMapping("/page")
    @Operation(summary = "Student Pagination")
    public ResponseEntity<PageResponse<StudentResponse>> getStudents(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "firstName") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        Page<StudentResponse> studentPage =
                studentService.getAll(page, size, sortBy, direction);

        PageResponse<StudentResponse> response = PageResponse.<StudentResponse>builder()
                .content(studentPage.getContent())
                .page(studentPage.getNumber())
                .size(studentPage.getSize())
                .totalElements(studentPage.getTotalElements())
                .totalPages(studentPage.getTotalPages())
                .first(studentPage.isFirst())
                .last(studentPage.isLast())
                .build();

        return ResponseEntity.ok(response);
    }
    /**
     * Search Students
     */
    @PostMapping("/search")
    @Operation(summary = "Search Students")
    public ResponseEntity<Page<StudentResponse>> searchStudents(
            @RequestBody StudentSearchRequest request) {

        return ResponseEntity.ok(
                studentService.search(request));
    }

    /**
     * Activate Student
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Student")
    public ResponseEntity<StudentResponse> activateStudent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentService.activate(id));
    }

    /**
     * Deactivate Student
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Student")
    public ResponseEntity<StudentResponse> deactivateStudent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentService.deactivate(id));
    }

    /**
     * Soft Delete Student
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Student")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id) {

        studentService.delete(id);

        return ResponseEntity.noContent().build();
    }

}