package com.smartexam.institution.settings.controller;

import com.smartexam.institution.settings.dto.CreateInstitutionSettingsRequest;
import com.smartexam.institution.settings.dto.InstitutionSettingsResponse;
import com.smartexam.institution.settings.dto.UpdateInstitutionSettingsRequest;
import com.smartexam.institution.settings.service.InstitutionSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/institution-settings")
@RequiredArgsConstructor
public class InstitutionSettingsController {

    private final InstitutionSettingsService institutionSettingsService;

    /**
     * Create institution settings
     */
    @PostMapping
    public ResponseEntity<InstitutionSettingsResponse> create(
            @Valid @RequestBody CreateInstitutionSettingsRequest request) {

        InstitutionSettingsResponse response =
                institutionSettingsService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Get institution settings by organization ID
     */
    @GetMapping
    public ResponseEntity<InstitutionSettingsResponse> getByOrganizationId(
            @RequestParam Long organizationId) {

        InstitutionSettingsResponse response =
                institutionSettingsService.getByOrganizationId(organizationId);

        return ResponseEntity.ok(response);
    }

    /**
     * Update institution settings by organization ID
     */
    @PutMapping
    public ResponseEntity<InstitutionSettingsResponse> update(
            @RequestParam Long organizationId,
            @Valid @RequestBody UpdateInstitutionSettingsRequest request) {

        InstitutionSettingsResponse response =
                institutionSettingsService.update(
                        organizationId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam Long organizationId) {

        institutionSettingsService.delete(organizationId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivate(
            @RequestParam Long organizationId) {

        institutionSettingsService.deactivate(organizationId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> activate(
            @RequestParam Long organizationId) {

        institutionSettingsService.activate(organizationId);

        return ResponseEntity.noContent().build();
    }
}