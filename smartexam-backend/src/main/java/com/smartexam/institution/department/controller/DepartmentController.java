
package com.smartexam.institution.department.controller;

import com.smartexam.common.dto.PageResponse;
import com.smartexam.institution.department.dto.DepartmentCreateRequest;
import com.smartexam.institution.department.dto.DepartmentResponse;
import com.smartexam.institution.department.dto.DepartmentUpdateRequest;

import com.smartexam.institution.department.service.DepartmentService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {


    private final DepartmentService departmentService;


    // =========================================================
    // CREATE
    // POST /api/v1/departments
    // =========================================================

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(

            @Valid
            @RequestBody
            DepartmentCreateRequest request

    ) {


        DepartmentResponse response =
                departmentService.create(
                        request
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


    // =========================================================
    // UPDATE
    // PUT /api/v1/departments/{id}
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            DepartmentUpdateRequest request

    ) {


        return ResponseEntity.ok(

                departmentService.update(
                        id,
                        request
                )

        );

    }


    // =========================================================
    // GET BY ID
    // GET /api/v1/departments/{id}
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getById(

            @PathVariable
            Long id

    ) {


        return ResponseEntity.ok(

                departmentService.getById(
                        id
                )

        );

    }


    // =========================================================
    // GET ALL
    // GET /api/v1/departments
    // =========================================================

    @GetMapping
    public ResponseEntity<@NotNull PageResponse<DepartmentResponse>> getDepartments(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "20"
            )
            int size,

            @RequestParam(
                    defaultValue = "name"
            )
            String sortBy,

            @RequestParam(
                    defaultValue = "asc"
            )
            String direction,

            @RequestParam(
                    required = false
            )
            Long organizationId,

            @RequestParam(
                    required = false
            )
            String search

    ) {


        return ResponseEntity.ok(

                departmentService.getDepartments(

                        page,

                        size,

                        sortBy,

                        direction,

                        organizationId,

                        search

                )

        );

    }


    // =========================================================
    // DELETE
    // DELETE /api/v1/departments/{id}
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(

            @PathVariable
            Long id

    ) {


        departmentService.delete(
                id
        );


        return ResponseEntity.noContent()
                .build();

    }


    // =========================================================
    // ACTIVATE
    // PATCH /api/v1/departments/{id}/activate
    // =========================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<DepartmentResponse> activate(

            @PathVariable
            Long id

    ) {


        return ResponseEntity.ok(

                departmentService.activate(
                        id
                )

        );

    }


    // =========================================================
    // DEACTIVATE
    // PATCH /api/v1/departments/{id}/deactivate
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<DepartmentResponse> deactivate(

            @PathVariable
            Long id

    ) {


        return ResponseEntity.ok(

                departmentService.deactivate(
                        id
                )

        );

    }

}