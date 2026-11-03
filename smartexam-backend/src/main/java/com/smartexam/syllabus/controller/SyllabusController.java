package com.smartexam.syllabus.controller;

import com.smartexam.syllabus.dto.SyllabusRequest;
import com.smartexam.syllabus.dto.SyllabusResponse;
import com.smartexam.syllabus.dto.SyllabusUpdateRequest;
import com.smartexam.syllabus.service.SyllabusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/syllabuses")
@RequiredArgsConstructor
public class SyllabusController {

    private final SyllabusService syllabusService;


    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<SyllabusResponse> create(
            @Valid @RequestBody SyllabusRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        syllabusService.create(request)
                );
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<SyllabusResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SyllabusUpdateRequest request
    ) {

        return ResponseEntity.ok(
                syllabusService.update(
                        id,
                        request
                )
        );
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    public ResponseEntity<List<SyllabusResponse>> getAll() {

        return ResponseEntity.ok(
                syllabusService.getAll()
        );
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<SyllabusResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                syllabusService.getById(id)
        );
    }


    // =========================================================
    // GET BY ORGANIZATION
    // =========================================================

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<SyllabusResponse>>
    getByOrganization(
            @PathVariable Long organizationId
    ) {

        return ResponseEntity.ok(
                syllabusService.getByOrganization(
                        organizationId
                )
        );
    }


    // =========================================================
    // GET BY CURRICULUM
    // =========================================================

    @GetMapping("/curriculum/{curriculumId}")
    public ResponseEntity<List<SyllabusResponse>>
    getByCurriculum(
            @PathVariable Long curriculumId
    ) {

        return ResponseEntity.ok(
                syllabusService.getByCurriculum(
                        curriculumId
                )
        );
    }


    // =========================================================
    // GET ACTIVE BY CURRICULUM
    // =========================================================

    @GetMapping("/curriculum/{curriculumId}/active")
    public ResponseEntity<List<SyllabusResponse>>
    getActiveByCurriculum(
            @PathVariable Long curriculumId
    ) {

        return ResponseEntity.ok(
                syllabusService.getActiveByCurriculum(
                        curriculumId
                )
        );
    }


    // =========================================================
    // GET BY CLASS SUBJECT
    // =========================================================

    @GetMapping("/class-subject/{classSubjectId}")
    public ResponseEntity<List<SyllabusResponse>>
    getByClassSubject(
            @PathVariable Long classSubjectId
    ) {

        return ResponseEntity.ok(
                syllabusService.getByClassSubject(
                        classSubjectId
                )
        );
    }


    // =========================================================
    // GET BY CURRICULUM + CLASS SUBJECT
    // =========================================================

    @GetMapping(
            "/curriculum/{curriculumId}/class-subject/{classSubjectId}"
    )
    public ResponseEntity<List<SyllabusResponse>>
    getByCurriculumAndClassSubject(
            @PathVariable Long curriculumId,
            @PathVariable Long classSubjectId
    ) {

        return ResponseEntity.ok(
                syllabusService
                        .getByCurriculumAndClassSubject(
                                curriculumId,
                                classSubjectId
                        )
        );
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<SyllabusResponse> activate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                syllabusService.activate(id)
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SyllabusResponse> deactivate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                syllabusService.deactivate(id)
        );
    }


    // =========================================================
    // DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        syllabusService.delete(id);

        return ResponseEntity.noContent().build();
    }
}