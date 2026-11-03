package com.smartexam.parentguardian.controller;

import com.smartexam.parentguardian.dto.ParentGuardianRequest;
import com.smartexam.parentguardian.dto.ParentGuardianResponse;
import com.smartexam.parentguardian.service.ParentGuardianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/parent-guardians")
@RequiredArgsConstructor
public class ParentGuardianController {

    private final ParentGuardianService parentGuardianService;

    // =========================================================
    // Create
    // =========================================================

    @PostMapping
    public ResponseEntity<ParentGuardianResponse> create(
            @Valid @RequestBody ParentGuardianRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(parentGuardianService.create(request));
    }

    // =========================================================
    // Update
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<ParentGuardianResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ParentGuardianRequest request) {

        return ResponseEntity.ok(
                parentGuardianService.update(id, request)
        );
    }

    // =========================================================
    // Get By ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ParentGuardianResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                parentGuardianService.getById(id)
        );
    }

    // =========================================================
    // Get All - Pagination
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<ParentGuardianResponse>> getAll(
            @PageableDefault(
                    size = 20,
                    sort = "firstName",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable) {

        return ResponseEntity.ok(
                parentGuardianService.getAll(pageable)
        );
    }

    // =========================================================
    // Deactivate
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id) {

        parentGuardianService.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // Activate
    // =========================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @PathVariable Long id) {

        parentGuardianService.activate(id);

        return ResponseEntity.noContent().build();
    }
}