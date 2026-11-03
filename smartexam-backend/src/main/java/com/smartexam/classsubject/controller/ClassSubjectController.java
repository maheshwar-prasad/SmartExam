package com.smartexam.classsubject.controller;

import com.smartexam.classsubject.dto.ClassSubjectRequest;
import com.smartexam.classsubject.dto.ClassSubjectResponse;
import com.smartexam.classsubject.dto.ClassSubjectSearchRequest;
import com.smartexam.classsubject.dto.ClassSubjectUpdateRequest;
import com.smartexam.classsubject.service.ClassSubjectService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/class-subjects")
@RequiredArgsConstructor
public class ClassSubjectController {

    private final ClassSubjectService classSubjectService;


    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<ClassSubjectResponse> create(
            @Valid @RequestBody ClassSubjectRequest request
    ) {

        ClassSubjectResponse response =
                classSubjectService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<ClassSubjectResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ClassSubjectUpdateRequest request
    ) {

        return ResponseEntity.ok(
                classSubjectService.update(
                        id,
                        request
                )
        );
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ClassSubjectResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                classSubjectService.getById(id)
        );
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    public ResponseEntity<List<ClassSubjectResponse>> getAll() {

        return ResponseEntity.ok(
                classSubjectService.getAll()
        );
    }


    // =========================================================
    // PAGINATION
    // =========================================================

    @GetMapping("/page")
    public ResponseEntity<Page<ClassSubjectResponse>> getPage(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size,

            @RequestParam(
                    defaultValue = "id"
            )
            String sortBy,

            @RequestParam(
                    defaultValue = "ASC"
            )
            String direction

    ) {

        return ResponseEntity.ok(
                classSubjectService.getPage(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }


    // =========================================================
    // SEARCH
    // =========================================================

    @PostMapping("/search")
    public ResponseEntity<Page<ClassSubjectResponse>> search(
            @RequestBody ClassSubjectSearchRequest request
    ) {

        return ResponseEntity.ok(
                classSubjectService.search(request)
        );
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ClassSubjectResponse> activate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                classSubjectService.activate(id)
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ClassSubjectResponse> deactivate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                classSubjectService.deactivate(id)
        );
    }


    // =========================================================
    // DELETE / SOFT DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        classSubjectService.delete(id);

        return ResponseEntity.noContent().build();
    }
}