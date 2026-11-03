package com.smartexam.examsubject.controller;

import com.smartexam.examsubject.dto.ExamSubjectRequest;
import com.smartexam.examsubject.dto.ExamSubjectResponse;
import com.smartexam.examsubject.dto.ExamSubjectSearchRequest;
import com.smartexam.examsubject.service.ExamSubjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam-subjects")
public class ExamSubjectController {

    private final ExamSubjectService examSubjectService;

    public ExamSubjectController(
            ExamSubjectService examSubjectService) {

        this.examSubjectService = examSubjectService;
    }

    /**
     * Create Exam Subject.
     */
    @PostMapping
    public ResponseEntity<ExamSubjectResponse> create(
            @Valid @RequestBody ExamSubjectRequest request) {

        ExamSubjectResponse response =
                examSubjectService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Update Exam Subject.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExamSubjectResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ExamSubjectRequest request) {

        return ResponseEntity.ok(
                examSubjectService.update(id, request)
        );
    }

    /**
     * Get paginated active Exam Subjects.
     *
     * Keep static paths before /{id}.
     */
    @GetMapping("/page")
    public ResponseEntity<Page<ExamSubjectResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                examSubjectService.getPage(page, size)
        );
    }

    /**
     * Get Exam Subjects by Exam ID.
     */
    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamSubjectResponse>> getByExamId(
            @PathVariable Long examId) {

        return ResponseEntity.ok(
                examSubjectService.getByExamId(examId)
        );
    }

    /**
     * Get Exam Subjects by Subject ID.
     */
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<ExamSubjectResponse>> getBySubjectId(
            @PathVariable Long subjectId) {

        return ResponseEntity.ok(
                examSubjectService.getBySubjectId(subjectId)
        );
    }

    /**
     * Get Exam Subjects by Organization ID.
     */
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<ExamSubjectResponse>>
    getByOrganizationId(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                examSubjectService
                        .getByOrganizationId(organizationId)
        );
    }

    /**
     * Get all active Exam Subjects.
     */
    @GetMapping
    public ResponseEntity<List<ExamSubjectResponse>> getAll() {

        return ResponseEntity.ok(
                examSubjectService.getAll()
        );
    }

    /**
     * Get Exam Subject by ID.
     *
     * Numeric constraint prevents "page", "exam", etc.
     * from being interpreted as an ID.
     */
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ExamSubjectResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examSubjectService.getById(id)
        );
    }

    /**
     * Activate Exam Subject.
     */
    @PatchMapping("/{id:\\d+}/activate")
    public ResponseEntity<ExamSubjectResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examSubjectService.activate(id)
        );
    }

    /**
     * Deactivate Exam Subject.
     */
    @PatchMapping("/{id:\\d+}/deactivate")
    public ResponseEntity<ExamSubjectResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examSubjectService.deactivate(id)
        );
    }

    /**
     * Delete Exam Subject.
     */
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        examSubjectService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ExamSubjectResponse>> search(
            @RequestBody ExamSubjectSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                examSubjectService.search(
                        request,
                        page,
                        size
                )
        );
    }
}