package com.smartexam.examresult.controller;

import com.smartexam.examresult.dto.ExamResultRequest;
import com.smartexam.examresult.dto.ExamResultResponse;
import com.smartexam.examresult.dto.ExamResultSearchRequest;
import com.smartexam.examresult.service.ExamResultService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam-results")
public class ExamResultController {

    private final ExamResultService examResultService;

    public ExamResultController(
            ExamResultService examResultService) {

        this.examResultService = examResultService;
    }

    /**
     * Generate exam result.
     */
    @PostMapping("/generate")
    public ResponseEntity<ExamResultResponse> generate(
            @Valid @RequestBody ExamResultRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        examResultService.generate(request)
                );
    }

    /**
     * Get result by ID.
     */
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ExamResultResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examResultService.getById(id)
        );
    }

    /**
     * Get all active results.
     */
    @GetMapping
    public ResponseEntity<List<ExamResultResponse>> getAll() {

        return ResponseEntity.ok(
                examResultService.getAll()
        );
    }

    /**
     * Get paginated results.
     */
    @GetMapping("/page")
    public ResponseEntity<Page<ExamResultResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                examResultService.getPage(page, size)
        );
    }

    /**
     * Get results by student.
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ExamResultResponse>>
    getByStudentId(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                examResultService.getByStudentId(studentId)
        );
    }

    /**
     * Get results by exam.
     */
    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamResultResponse>>
    getByExamId(
            @PathVariable Long examId) {

        return ResponseEntity.ok(
                examResultService.getByExamId(examId)
        );
    }

    /**
     * Get result for a student and exam.
     */
    @GetMapping("/student/{studentId}/exam/{examId}")
    public ResponseEntity<ExamResultResponse>
    getByStudentAndExam(
            @PathVariable Long studentId,
            @PathVariable Long examId) {

        return ResponseEntity.ok(
                examResultService.getByStudentAndExam(
                        studentId,
                        examId
                )
        );
    }

    /**
     * Get results by organization.
     */
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<ExamResultResponse>>
    getByOrganizationId(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                examResultService.getByOrganizationId(
                        organizationId
                )
        );
    }

    /**
     * Publish result.
     */
    @PatchMapping("/{id:\\d+}/publish")
    public ResponseEntity<ExamResultResponse> publish(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examResultService.publish(id)
        );
    }

    /**
     * Unpublish result.
     */
    @PatchMapping("/{id:\\d+}/unpublish")
    public ResponseEntity<ExamResultResponse> unpublish(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examResultService.unpublish(id)
        );
    }

    /**
     * Delete result.
     */
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        examResultService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ExamResultResponse>> search(
            @RequestBody ExamResultSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                examResultService.search(
                        request,
                        page,
                        size
                )
        );
    }
}