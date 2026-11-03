package com.smartexam.institution.program.controller;

import com.smartexam.institution.program.dto.ProgramCreateRequest;
import com.smartexam.institution.program.dto.ProgramResponse;
import com.smartexam.institution.program.dto.ProgramUpdateRequest;
import com.smartexam.institution.program.service.ProgramService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/programs")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    @PostMapping
    public ResponseEntity<ProgramResponse> createProgram(
            @Valid @RequestBody ProgramCreateRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(programService.createProgram(request));
    }

    @GetMapping("/{programId}")
    public ResponseEntity<ProgramResponse> getProgramById(
            @RequestParam Long organizationId,
            @PathVariable Long programId
    ) {

        return ResponseEntity.ok(
                programService.getProgramById(
                        programId
                )
        );
    }

    @GetMapping
    public ResponseEntity<Page<ProgramResponse>> getAllPrograms(
            @RequestParam Long organizationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "programName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        return ResponseEntity.ok(
                programService.getAllPrograms(
                        organizationId,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @PutMapping("/{programId}")
    public ResponseEntity<ProgramResponse> updateProgram(
            @PathVariable Long programId,
            @Valid @RequestBody ProgramUpdateRequest request
    ) {

        return ResponseEntity.ok(
                programService.updateProgram(
                        programId,
                        request
                )
        );
    }

    @DeleteMapping("/{programId}")
    public ResponseEntity<Void> deleteProgram(
            @RequestParam Long organizationId,
            @PathVariable Long programId
    ) {

        programService.deleteProgram(
                programId
        );

        return ResponseEntity.noContent().build();
    }
}