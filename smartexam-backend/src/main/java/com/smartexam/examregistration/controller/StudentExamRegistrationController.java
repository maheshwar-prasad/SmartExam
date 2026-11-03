package com.smartexam.examregistration.controller;

import com.smartexam.examregistration.dto.StudentExamRegistrationRequest;
import com.smartexam.examregistration.dto.StudentExamRegistrationResponse;
import com.smartexam.examregistration.dto.StudentExamRegistrationSearchRequest;
import com.smartexam.examregistration.service.StudentExamRegistrationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-exam-registrations")
public class StudentExamRegistrationController {

    private final StudentExamRegistrationService registrationService;

    public StudentExamRegistrationController(
            StudentExamRegistrationService registrationService) {

        this.registrationService = registrationService;
    }

    /**
     * Register student for an exam.
     */
    @PostMapping
    public ResponseEntity<StudentExamRegistrationResponse> create(
            @Valid @RequestBody StudentExamRegistrationRequest request) {

        StudentExamRegistrationResponse response =
                registrationService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Update registration.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentExamRegistrationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentExamRegistrationRequest request) {

        return ResponseEntity.ok(
                registrationService.update(id, request)
        );
    }

    /**
     * Get registration by ID.
     */
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<StudentExamRegistrationResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                registrationService.getById(id)
        );
    }

    /**
     * Get all active registrations.
     */
    @GetMapping
    public ResponseEntity<List<StudentExamRegistrationResponse>> getAll() {

        return ResponseEntity.ok(
                registrationService.getAll()
        );
    }

    /**
     * Get paginated registrations.
     */
    @GetMapping("/page")
    public ResponseEntity<Page<StudentExamRegistrationResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                registrationService.getPage(page, size)
        );
    }

    /**
     * Get registrations by student.
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentExamRegistrationResponse>>
    getByStudentId(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                registrationService.getByStudentId(studentId)
        );
    }

    /**
     * Get registrations by exam.
     */
    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<StudentExamRegistrationResponse>>
    getByExamId(
            @PathVariable Long examId) {

        return ResponseEntity.ok(
                registrationService.getByExamId(examId)
        );
    }

    /**
     * Get registrations by organization.
     */
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<StudentExamRegistrationResponse>>
    getByOrganizationId(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                registrationService.getByOrganizationId(
                        organizationId
                )
        );
    }

    /**
     * Cancel registration.
     */
    @PatchMapping("/{id:\\d+}/cancel")
    public ResponseEntity<StudentExamRegistrationResponse> cancel(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                registrationService.cancel(id)
        );
    }

    /**
     * Activate registration.
     */
    @PatchMapping("/{id:\\d+}/activate")
    public ResponseEntity<StudentExamRegistrationResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                registrationService.activate(id)
        );
    }

    /**
     * Deactivate registration.
     */
    @PatchMapping("/{id:\\d+}/deactivate")
    public ResponseEntity<StudentExamRegistrationResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                registrationService.deactivate(id)
        );
    }

    /**
     * Delete registration.
     */
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        registrationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<StudentExamRegistrationResponse>> search(
            @RequestBody StudentExamRegistrationSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                registrationService.search(
                        request,
                        page,
                        size
                )
        );
    }
}