package com.smartexam.academicstructure.controller;

import com.smartexam.academicstructure.dto.AcademicStructureRequest;
import com.smartexam.academicstructure.dto.AcademicStructureResponse;
import com.smartexam.academicstructure.dto.AcademicStructureUpdateRequest;
import com.smartexam.academicstructure.service.AcademicStructureService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic-structures")
@RequiredArgsConstructor
public class AcademicStructureController {

    private final AcademicStructureService service;


    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<AcademicStructureResponse> create(
            @Valid @RequestBody AcademicStructureRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<AcademicStructureResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AcademicStructureUpdateRequest request) {

        return ResponseEntity.ok(
                service.update(id, request)
        );
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    public ResponseEntity<List<AcademicStructureResponse>> getAll() {

        return ResponseEntity.ok(
                service.getAll()
        );
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<AcademicStructureResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getById(id)
        );
    }


    // =========================================================
    // BY ORGANIZATION
    // =========================================================

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<AcademicStructureResponse>>
    getByOrganization(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                service.getByOrganization(
                        organizationId
                )
        );
    }


    // =========================================================
    // BY ACADEMIC SESSION
    // =========================================================

    @GetMapping("/session/{academicSessionId}")
    public ResponseEntity<List<AcademicStructureResponse>>
    getByAcademicSession(
            @PathVariable Long academicSessionId) {

        return ResponseEntity.ok(
                service.getByAcademicSession(
                        academicSessionId
                )
        );
    }


    // =========================================================
    // BY CLASS
    // =========================================================

    @GetMapping("/class/{classRoomId}")
    public ResponseEntity<List<AcademicStructureResponse>>
    getByClassRoom(
            @PathVariable Long classRoomId) {

        return ResponseEntity.ok(
                service.getByClassRoom(
                        classRoomId
                )
        );
    }


    // =========================================================
    // BY SECTION
    // =========================================================

    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<AcademicStructureResponse>>
    getBySection(
            @PathVariable Long sectionId) {

        return ResponseEntity.ok(
                service.getBySection(
                        sectionId
                )
        );
    }


    // =========================================================
    // SESSION + CLASS
    // =========================================================

    @GetMapping(
            "/session/{academicSessionId}/class/{classRoomId}"
    )
    public ResponseEntity<List<AcademicStructureResponse>>
    getBySessionAndClass(
            @PathVariable Long academicSessionId,
            @PathVariable Long classRoomId) {

        return ResponseEntity.ok(
                service.getBySessionAndClass(
                        academicSessionId,
                        classRoomId
                )
        );
    }


    // =========================================================
    // ACTIVE BY ORGANIZATION
    // =========================================================

    @GetMapping("/organization/{organizationId}/active")
    public ResponseEntity<List<AcademicStructureResponse>>
    getActiveByOrganization(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                service.getActiveByOrganization(
                        organizationId
                )
        );
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AcademicStructureResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.activate(id)
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<AcademicStructureResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deactivate(id)
        );
    }


    // =========================================================
    // DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}