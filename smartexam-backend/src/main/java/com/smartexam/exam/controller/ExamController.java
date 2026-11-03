package com.smartexam.exam.controller;

import com.smartexam.exam.dto.ExamRequest;
import com.smartexam.exam.dto.ExamResponse;
import com.smartexam.exam.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<ExamResponse> create(
            @Valid @RequestBody ExamRequest request) {

        ExamResponse response = examService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ExamRequest request) {

        return ResponseEntity.ok(
                examService.update(id, request)
        );
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ExamResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                examService.getPage(page, size)
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<ExamResponse>> getByOrganizationId(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                examService.getByOrganizationId(organizationId)
        );
    }

    @GetMapping("/academic-session/{academicSessionId}")
    public ResponseEntity<List<ExamResponse>> getByAcademicSessionId(
            @PathVariable Long academicSessionId) {

        return ResponseEntity.ok(
                examService.getByAcademicSessionId(academicSessionId)
        );
    }

    @GetMapping("/class-section")
    public ResponseEntity<List<ExamResponse>> getByClassAndSection(
            @RequestParam Long academicSessionId,
            @RequestParam Long classRoomId,
            @RequestParam Long sectionId) {

        return ResponseEntity.ok(
                examService.getByClassAndSection(
                        academicSessionId,
                        classRoomId,
                        sectionId
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<ExamResponse>> getAll() {

        return ResponseEntity.ok(
                examService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examService.getById(id)
        );
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ExamResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examService.activate(id)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ExamResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examService.deactivate(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        examService.delete(id);

        return ResponseEntity.noContent().build();
    }
}