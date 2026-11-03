package com.smartexam.organization.controller;

import com.smartexam.auth.dto.ApiMessage;
import com.smartexam.organization.dto.OrganizationRequest;
import com.smartexam.organization.dto.OrganizationResponse;
import com.smartexam.organization.dto.OrganizationUpdateRequest;
import com.smartexam.organization.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationResponse> create(
            @Valid @RequestBody OrganizationRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(organizationService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                organizationService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<OrganizationResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                organizationService.getAll(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationUpdateRequest request) {

        return ResponseEntity.ok(
                organizationService.update(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiMessage> delete(
            @PathVariable Long id) {

        organizationService.delete(id);

        return ResponseEntity.ok(
                ApiMessage.builder()
                        .success(true)
                        .message(
                                "Organization deactivated successfully"
                        )
                        .build()
        );
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiMessage> activate(
            @PathVariable Long id) {

        organizationService.activate(id);

        return ResponseEntity.ok(
                ApiMessage.builder()
                        .success(true)
                        .message(
                                "Organization activated successfully"
                        )
                        .build()
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiMessage> deactivate(
            @PathVariable Long id) {

        organizationService.deactivate(id);

        return ResponseEntity.ok(
                ApiMessage.builder()
                        .success(true)
                        .message(
                                "Organization deactivated successfully"
                        )
                        .build()
        );
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countActive() {

        return ResponseEntity.ok(
                organizationService.countActive()
        );
    }
}