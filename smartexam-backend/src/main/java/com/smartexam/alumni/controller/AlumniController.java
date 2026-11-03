package com.smartexam.alumni.controller;

import com.smartexam.alumni.dto.AlumniRequest;
import com.smartexam.alumni.dto.AlumniResponse;
import com.smartexam.alumni.service.AlumniService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alumni")
@RequiredArgsConstructor
@Tag(
        name = "Alumni",
        description = "Alumni management APIs"
)
public class AlumniController {

    private final AlumniService alumniService;

    @PostMapping
    @Operation(summary = "Create alumni")
    public ResponseEntity<AlumniResponse> create(
            @Valid
            @RequestBody
            AlumniRequest request
    ) {

        return ResponseEntity.ok(
                alumniService.create(request)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update alumni")
    public ResponseEntity<AlumniResponse> update(
            @PathVariable Long id,

            @Valid
            @RequestBody
            AlumniRequest request
    ) {

        return ResponseEntity.ok(
                alumniService.update(id, request)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get alumni by ID")
    public ResponseEntity<AlumniResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                alumniService.getById(id)
        );
    }

    @GetMapping
    @Operation(summary = "Get alumni")
    public ResponseEntity<Page<AlumniResponse>> getAll(
            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            ) int page,

            @RequestParam(
                    name = "size",
                    defaultValue = "20"
            ) int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        return ResponseEntity.ok(
                alumniService.getAll(pageable)
        );
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate alumni")
    public ResponseEntity<Void> activate(
            @PathVariable Long id
    ) {

        alumniService.activate(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate alumni")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id
    ) {

        alumniService.deactivate(id);

        return ResponseEntity.noContent().build();
    }
}