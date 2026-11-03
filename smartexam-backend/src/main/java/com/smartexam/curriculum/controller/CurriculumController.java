package com.smartexam.curriculum.controller;

import com.smartexam.curriculum.dto.CurriculumRequest;
import com.smartexam.curriculum.dto.CurriculumResponse;
import com.smartexam.curriculum.dto.CurriculumSubjectRequest;
import com.smartexam.curriculum.dto.CurriculumSubjectResponse;
import com.smartexam.curriculum.dto.CurriculumUpdateRequest;
import com.smartexam.curriculum.service.CurriculumService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/curriculums")
@RequiredArgsConstructor
public class CurriculumController {

    private final CurriculumService curriculumService;


    // ============================================================
    // CREATE
    // ============================================================

    @PostMapping
    public ResponseEntity<CurriculumResponse> create(
            @Valid @RequestBody CurriculumRequest request
    ) {

        CurriculumResponse response =
                curriculumService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ============================================================
    // UPDATE
    // ============================================================

    @PutMapping("/{id}")
    public ResponseEntity<CurriculumResponse> update(
            @PathVariable Long id,

            @Valid @RequestBody
            CurriculumUpdateRequest request
    ) {

        return ResponseEntity.ok(
                curriculumService.update(
                        id,
                        request
                )
        );
    }


    // ============================================================
    // GET ALL
    // ============================================================

    @GetMapping
    public ResponseEntity<List<CurriculumResponse>> getAll() {

        return ResponseEntity.ok(
                curriculumService.getAll()
        );
    }


    // ============================================================
    // GET BY ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<CurriculumResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                curriculumService.getById(id)
        );
    }


    // ============================================================
    // GET BY ORGANIZATION
    // ============================================================

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<CurriculumResponse>>
    getByOrganization(
            @PathVariable Long organizationId
    ) {

        return ResponseEntity.ok(
                curriculumService.getByOrganization(
                        organizationId
                )
        );
    }


    // ============================================================
    // GET BY ACADEMIC SESSION
    // ============================================================

    @GetMapping("/session/{academicSessionId}")
    public ResponseEntity<List<CurriculumResponse>>
    getByAcademicSession(
            @PathVariable Long academicSessionId
    ) {

        return ResponseEntity.ok(
                curriculumService.getByAcademicSession(
                        academicSessionId
                )
        );
    }


    // ============================================================
    // GET BY CLASS
    // ============================================================

    @GetMapping("/class/{classRoomId}")
    public ResponseEntity<List<CurriculumResponse>>
    getByClassRoom(
            @PathVariable Long classRoomId
    ) {

        return ResponseEntity.ok(
                curriculumService.getByClassRoom(
                        classRoomId
                )
        );
    }


    // ============================================================
    // GET BY SESSION + CLASS
    // ============================================================

    @GetMapping(
            "/session/{academicSessionId}/class/{classRoomId}"
    )
    public ResponseEntity<List<CurriculumResponse>>
    getBySessionAndClass(
            @PathVariable Long academicSessionId,

            @PathVariable Long classRoomId
    ) {

        return ResponseEntity.ok(
                curriculumService.getBySessionAndClass(
                        academicSessionId,
                        classRoomId
                )
        );
    }


    // ============================================================
    // ACTIVATE
    // ============================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CurriculumResponse> activate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                curriculumService.activate(id)
        );
    }


    // ============================================================
    // DEACTIVATE
    // ============================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CurriculumResponse> deactivate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                curriculumService.deactivate(id)
        );
    }


    // ============================================================
    // DELETE
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        curriculumService.delete(id);

        return ResponseEntity.noContent().build();
    }


    // ============================================================
    // GET CURRICULUM SUBJECTS
    // ============================================================

    @GetMapping("/{curriculumId}/subjects")
    public ResponseEntity<List<CurriculumSubjectResponse>>
    getSubjects(
            @PathVariable Long curriculumId
    ) {

        return ResponseEntity.ok(
                curriculumService.getSubjects(
                        curriculumId
                )
        );
    }


    // ============================================================
    // ADD SUBJECT TO CURRICULUM
    // ============================================================

    @PostMapping("/{curriculumId}/subjects")
    public ResponseEntity<CurriculumSubjectResponse>
    addSubject(
            @PathVariable Long curriculumId,

            @Valid @RequestBody
            CurriculumSubjectRequest request
    ) {

        CurriculumSubjectResponse response =
                curriculumService.addSubject(
                        curriculumId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ============================================================
    // UPDATE CURRICULUM SUBJECT
    // ============================================================

    @PutMapping("/subjects/{subjectId}")
    public ResponseEntity<CurriculumSubjectResponse>
    updateSubject(
            @PathVariable Long subjectId,

            @Valid @RequestBody
            CurriculumSubjectRequest request
    ) {

        return ResponseEntity.ok(
                curriculumService.updateSubject(
                        subjectId,
                        request
                )
        );
    }


    // ============================================================
    // ACTIVATE CURRICULUM SUBJECT
    // ============================================================

    @PatchMapping(
            "/subjects/{subjectId}/activate"
    )
    public ResponseEntity<CurriculumSubjectResponse>
    activateSubject(
            @PathVariable Long subjectId
    ) {

        return ResponseEntity.ok(
                curriculumService.activateSubject(
                        subjectId
                )
        );
    }


    // ============================================================
    // DEACTIVATE CURRICULUM SUBJECT
    // ============================================================

    @PatchMapping(
            "/subjects/{subjectId}/deactivate"
    )
    public ResponseEntity<CurriculumSubjectResponse>
    deactivateSubject(
            @PathVariable Long subjectId
    ) {

        return ResponseEntity.ok(
                curriculumService.deactivateSubject(
                        subjectId
                )
        );
    }


    // ============================================================
    // DELETE CURRICULUM SUBJECT
    // ============================================================

    @DeleteMapping(
            "/subjects/{subjectId}"
    )
    public ResponseEntity<Void> deleteSubject(
            @PathVariable Long subjectId
    ) {

        curriculumService.deleteSubject(
                subjectId
        );

        return ResponseEntity.noContent().build();
    }
}