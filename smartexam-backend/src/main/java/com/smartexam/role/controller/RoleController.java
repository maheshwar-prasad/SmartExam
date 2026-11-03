package com.smartexam.role.controller;

import com.smartexam.role.dto.RoleRequest;
import com.smartexam.role.dto.RoleResponse;
import com.smartexam.role.dto.RoleUpdateRequest;
import com.smartexam.role.service.RoleService;
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
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Role API", description = "Role Management APIs")
public class RoleController {

    private final RoleService roleService;

    /**
     * Create Role
     */
    @PostMapping
    @Operation(summary = "Create Role")
    public ResponseEntity<RoleResponse> createRole(
            @Valid @RequestBody RoleRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.create(request));
    }

    /**
     * Update Role
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update Role")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequest request) {

        return ResponseEntity.ok(
                roleService.update(id, request));
    }

    /**
     * Get Role By Id
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get Role By Id")
    public ResponseEntity<RoleResponse> getRole(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                roleService.getById(id));
    }

    /**
     * Get All Roles
     */
    @GetMapping
    @Operation(summary = "Get All Roles")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {

        return ResponseEntity.ok(
                roleService.getAll());
    }

    /**
     * Pagination
     */
    @GetMapping("/page")
    @Operation(summary = "Role Pagination")
    public ResponseEntity<Page<RoleResponse>> getRoles(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "name")
            String sortBy,

            @RequestParam(defaultValue = "ASC")
            String direction) {

        return ResponseEntity.ok(
                roleService.getAll(
                        page,
                        size,
                        sortBy,
                        direction));
    }

    /**
     * Search Roles
     */
    @GetMapping("/search")
    @Operation(summary = "Search Roles")
    public ResponseEntity<Page<RoleResponse>> searchRoles(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        return ResponseEntity.ok(
                roleService.search(keyword, page, size));
    }

    /**
     * Activate Role
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Role")
    public ResponseEntity<RoleResponse> activateRole(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                roleService.activate(id));
    }

    /**
     * Deactivate Role
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Role")
    public ResponseEntity<RoleResponse> deactivateRole(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                roleService.deactivate(id));
    }

    /**
     * Delete Role (Soft Delete)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Role")
    public ResponseEntity<Void> deleteRole(
            @PathVariable Long id) {

        roleService.delete(id);

        return ResponseEntity.noContent().build();
    }

}