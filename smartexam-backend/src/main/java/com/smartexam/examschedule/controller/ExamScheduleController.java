package com.smartexam.examschedule.controller;

import com.smartexam.examschedule.dto.ExamScheduleRequest;
import com.smartexam.examschedule.dto.ExamScheduleResponse;
import com.smartexam.examschedule.dto.ExamScheduleSearchRequest;
import com.smartexam.examschedule.service.ExamScheduleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam-schedules")
public class ExamScheduleController {

    private final ExamScheduleService examScheduleService;

    public ExamScheduleController(
            ExamScheduleService examScheduleService) {

        this.examScheduleService = examScheduleService;
    }

    /**
     * Create Exam Schedule.
     */
    @PostMapping
    public ResponseEntity<ExamScheduleResponse> create(
            @Valid @RequestBody ExamScheduleRequest request) {

        ExamScheduleResponse response =
                examScheduleService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Update Exam Schedule.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExamScheduleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ExamScheduleRequest request) {

        return ResponseEntity.ok(
                examScheduleService.update(id, request)
        );
    }

    /**
     * Get paginated active schedules.
     */
    @GetMapping("/page")
    public ResponseEntity<Page<ExamScheduleResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                examScheduleService.getPage(page, size)
        );
    }

    /**
     * Get schedules by Exam ID.
     */
    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamScheduleResponse>> getByExamId(
            @PathVariable Long examId) {

        return ResponseEntity.ok(
                examScheduleService.getByExamId(examId)
        );
    }

    /**
     * Get schedules by Exam Subject ID.
     */
    @GetMapping("/exam-subject/{examSubjectId}")
    public ResponseEntity<List<ExamScheduleResponse>>
    getByExamSubjectId(
            @PathVariable Long examSubjectId) {

        return ResponseEntity.ok(
                examScheduleService
                        .getByExamSubjectId(examSubjectId)
        );
    }

    /**
     * Get schedules by Organization ID.
     */
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<ExamScheduleResponse>>
    getByOrganizationId(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                examScheduleService
                        .getByOrganizationId(organizationId)
        );
    }

    /**
     * Get all active schedules.
     */
    @GetMapping
    public ResponseEntity<List<ExamScheduleResponse>> getAll() {

        return ResponseEntity.ok(
                examScheduleService.getAll()
        );
    }

    /**
     * Get Exam Schedule by ID.
     */
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ExamScheduleResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examScheduleService.getById(id)
        );
    }

    /**
     * Activate Exam Schedule.
     */
    @PatchMapping("/{id:\\d+}/activate")
    public ResponseEntity<ExamScheduleResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examScheduleService.activate(id)
        );
    }

    /**
     * Deactivate Exam Schedule.
     */
    @PatchMapping("/{id:\\d+}/deactivate")
    public ResponseEntity<ExamScheduleResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examScheduleService.deactivate(id)
        );
    }

    /**
     * Delete Exam Schedule.
     */
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        examScheduleService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ExamScheduleResponse>> search(
            @RequestBody ExamScheduleSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                examScheduleService.search(
                        request,
                        page,
                        size
                )
        );
    }
}