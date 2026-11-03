package com.smartexam.marks.controller;

import com.smartexam.marks.dto.StudentMarksRequest;
import com.smartexam.marks.dto.StudentMarksResponse;
import com.smartexam.marks.dto.StudentMarksSearchRequest;
import com.smartexam.marks.service.StudentMarksService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-marks")
public class StudentMarksController {

    private final StudentMarksService studentMarksService;

    public StudentMarksController(
            StudentMarksService studentMarksService) {

        this.studentMarksService = studentMarksService;
    }

    @PostMapping
    public ResponseEntity<StudentMarksResponse> create(
            @Valid @RequestBody StudentMarksRequest request) {

        StudentMarksResponse response =
                studentMarksService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentMarksResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentMarksRequest request) {

        return ResponseEntity.ok(
                studentMarksService.update(id, request)
        );
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<StudentMarksResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentMarksService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<StudentMarksResponse>> getAll() {

        return ResponseEntity.ok(
                studentMarksService.getAll()
        );
    }

    @GetMapping("/page")
    public ResponseEntity<Page<StudentMarksResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                studentMarksService.getPage(page, size)
        );
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentMarksResponse>> getByStudentId(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                studentMarksService.getByStudentId(studentId)
        );
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<StudentMarksResponse>> getByExamId(
            @PathVariable Long examId) {

        return ResponseEntity.ok(
                studentMarksService.getByExamId(examId)
        );
    }

    @GetMapping("/exam-subject/{examSubjectId}")
    public ResponseEntity<List<StudentMarksResponse>>
    getByExamSubjectId(
            @PathVariable Long examSubjectId) {

        return ResponseEntity.ok(
                studentMarksService.getByExamSubjectId(
                        examSubjectId
                )
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<StudentMarksResponse>>
    getByOrganizationId(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                studentMarksService.getByOrganizationId(
                        organizationId
                )
        );
    }

    @PatchMapping("/{id:\\d+}/publish")
    public ResponseEntity<StudentMarksResponse> publish(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentMarksService.publish(id)
        );
    }

    @PatchMapping("/{id:\\d+}/unpublish")
    public ResponseEntity<StudentMarksResponse> unpublish(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentMarksService.unpublish(id)
        );
    }

    @PatchMapping("/{id:\\d+}/activate")
    public ResponseEntity<StudentMarksResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentMarksService.activate(id)
        );
    }

    @PatchMapping("/{id:\\d+}/deactivate")
    public ResponseEntity<StudentMarksResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentMarksService.deactivate(id)
        );
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        studentMarksService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<StudentMarksResponse>> search(
            @RequestBody StudentMarksSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                studentMarksService.search(
                        request,
                        page,
                        size
                )
        );
    }
}