package com.smartexam.institution.campuses.controller;

import com.smartexam.institution.campuses.dto.CampusCreateRequest;
import com.smartexam.institution.campuses.dto.CampusResponse;
import com.smartexam.institution.campuses.dto.CampusUpdateRequest;

import com.smartexam.institution.campuses.service.CampusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/campuses")
@RequiredArgsConstructor
public class CampusController {

    private final CampusService campusService;

    /**
     * Create campus
     */
    @PostMapping
    public ResponseEntity<@NotNull CampusResponse> createCampus(
            @Valid @RequestBody CampusCreateRequest request) {

        CampusResponse response =
                campusService.createCampus(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Update campus
     */
    @PutMapping("/{id}")
    public ResponseEntity<@NotNull CampusResponse> updateCampus(
            @PathVariable Long id,
            @Valid @RequestBody CampusUpdateRequest request) {

        CampusResponse response =
                campusService.updateCampus(id, request);

        return ResponseEntity.ok(response);
    }

    /**
     * Get campus by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<@NotNull CampusResponse> getCampusById(
            @PathVariable Long id) {

        CampusResponse response =
                campusService.getCampusById(id);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all campuses
     */
    @GetMapping
    public ResponseEntity<@NotNull List<CampusResponse>> getAllCampuses(
        ) {

        List<CampusResponse> response =
                campusService.getAllCampuses();

        return ResponseEntity.ok(response);
    }

    /**
     * Get campuses by organization
     */
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<@NotNull List<CampusResponse>> getCampusesByOrganization(
            @PathVariable Long organizationId) {

        List<CampusResponse> response =
                campusService.getCampusesByOrganization(
                        organizationId
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Activate campus
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<@NotNull CampusResponse> activateCampus(
            @PathVariable Long id) {

        CampusResponse response =
                campusService.activateCampus(id);

        return ResponseEntity.ok(response);
    }

    /**
     * Deactivate campus
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<@NotNull CampusResponse> deactivateCampus(
            @PathVariable Long id) {

        CampusResponse response =
                campusService.deactivateCampus(id);

        return ResponseEntity.ok(response);
    }

    /**
     * Delete campus
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<@NotNull Void> deleteCampus(
            @PathVariable Long id) {

        campusService.deleteCampus(id);

        return ResponseEntity.noContent().build();
    }
}