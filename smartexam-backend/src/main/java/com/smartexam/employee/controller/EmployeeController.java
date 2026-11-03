package com.smartexam.employee.controller;

import com.smartexam.employee.dto.EmployeeRequest;
import com.smartexam.employee.dto.EmployeeResponse;
import com.smartexam.employee.enums.EmployeeType;
import com.smartexam.employee.service.EmployeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    // Create employee
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response =
                employeeService.createEmployee(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Update employee
    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable @Positive Long employeeId,
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(employeeId, request)
        );
    }

    // Get employee by ID
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable @Positive Long employeeId) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(employeeId)
        );
    }

    // Get paginated employees for an organization
    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getEmployees(
            @RequestParam @Positive Long organizationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        validatePagination(page, size);
        validateSortField(sortBy);

        Sort.Direction direction =
                "desc".equalsIgnoreCase(sortDirection)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );

        return ResponseEntity.ok(
                employeeService.getEmployees(organizationId, pageable)
        );
    }

    // Search and filter employees
    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponse>> searchEmployees(
            @RequestParam @Positive Long organizationId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EmployeeType employeeType,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        validatePagination(page, size);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "firstName")
        );

        return ResponseEntity.ok(
                employeeService.searchEmployees(
                        organizationId,
                        search,
                        employeeType,
                        active,
                        pageable
                )
        );
    }

    // Soft deactivate employee
    @PatchMapping("/{employeeId}/deactivate")
    public ResponseEntity<Void> deactivateEmployee(
            @PathVariable @Positive Long employeeId) {

        employeeService.deactivateEmployee(employeeId);

        return ResponseEntity.noContent().build();
    }

    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }
    }

    private void validateSortField(String sortBy) {
        if (!java.util.Set.of(
                "id",
                "employeeCode",
                "firstName",
                "lastName",
                "joiningDate",
                "employeeType",
                "createdAt"
        ).contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Unsupported sort field: " + sortBy
            );
        }
    }
}