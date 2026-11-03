package com.smartexam.permission.controller;

import com.smartexam.permission.dto.PermissionRequest;
import com.smartexam.permission.dto.PermissionResponse;
import com.smartexam.permission.dto.PermissionSearchRequest;
import com.smartexam.permission.dto.PermissionUpdateRequest;
import com.smartexam.permission.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Tag(name = "Permission API", description = "Permission Management APIs")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * Create Permission
     */
    @PostMapping
    @Operation(summary = "Create Permission")
    public ResponseEntity<PermissionResponse> createPermission(
            @Valid @RequestBody PermissionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(permissionService.create(request));
    }

    /**
     * Update Permission
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update Permission")
    public ResponseEntity<PermissionResponse> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionUpdateRequest request) {

        return ResponseEntity.ok(
                permissionService.update(id, request));
    }

    /**
     * Get Permission By Id
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get Permission By Id")
    public ResponseEntity<PermissionResponse> getPermission(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                permissionService.getById(id));
    }

    /**
     * Get All Permissions
     */
    @GetMapping
    @Operation(summary = "Get All Permissions")
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {

        return ResponseEntity.ok(
                permissionService.getAll());
    }

    /**
     * Pagination
     */
    @GetMapping("/page")
    @Operation(summary = "Permission Pagination")
    public ResponseEntity<Page<PermissionResponse>> getPermissions(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "name")
            String sortBy,

            @RequestParam(defaultValue = "ASC")
            String direction) {

        return ResponseEntity.ok(
                permissionService.getAll(
                        page,
                        size,
                        sortBy,
                        direction));
    }

    /**
     * Search Permissions
     */
    @PostMapping("/search")
    @Operation(summary = "Search Permissions")
    public ResponseEntity<Page<PermissionResponse>> searchPermissions(
            @RequestBody PermissionSearchRequest request) {

        return ResponseEntity.ok(
                permissionService.search(request));
    }

    /**
     * Activate Permission
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Permission")
    public ResponseEntity<PermissionResponse> activatePermission(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                permissionService.activate(id));
    }

    /**
     * Deactivate Permission
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Permission")
    public ResponseEntity<PermissionResponse> deactivatePermission(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                permissionService.deactivate(id));
    }

    /**
     * Delete Permission (Soft Delete)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Permission")
    public ResponseEntity<Void> deletePermission(
            @PathVariable Long id) {

        permissionService.delete(id);

        return ResponseEntity.noContent().build();
    }

}