package com.smartexam.staff.directory.controller;

import com.smartexam.staff.directory.dto.StaffCreateRequest;
import com.smartexam.staff.directory.dto.StaffPageResponse;
import com.smartexam.staff.directory.dto.StaffResponse;
import com.smartexam.staff.directory.dto.StaffUpdateRequest;
import com.smartexam.staff.directory.enums.StaffStatus;
import com.smartexam.staff.directory.service.StaffService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/staff")
@RequiredArgsConstructor
@Tag(
        name = "Staff Directory",
        description = "APIs for managing organization staff members"
)
public class StaffController {

    private final StaffService staffService;

    @Operation(
            summary = "Create staff member",
            description = "Creates a new staff member for an organization"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Staff member created successfully",
                    content = @Content(
                            schema = @Schema(
                                    implementation = StaffResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Duplicate staff code, employee code or email"
            )
    })
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<StaffResponse> createStaff(
            @PathVariable Long organizationId,
            @Valid @RequestPart("data") StaffCreateRequest request,
            @RequestPart(
                    value = "photo",
                    required = false
            ) MultipartFile photo
    ) {

        System.out.println(
                "========== CREATE STAFF =========="
        );

        System.out.println(
                "Organization ID: " + organizationId
        );

        System.out.println(
                "Staff Code: " + request.getStaffCode()
        );

        System.out.println(
                "First Name: " + request.getFirstName()
        );

        System.out.println(
                "Photo: " +
                        (photo != null
                                ? photo.getOriginalFilename()
                                : "NO PHOTO")
        );

        System.out.println(
                "Photo Content Type: " +
                        (photo != null
                                ? photo.getContentType()
                                : "N/A")
        );

        StaffResponse response =
                staffService.createStaff(
                        organizationId,
                        request,
                        photo
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Update staff member",
            description = "Updates an existing staff member"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Staff member updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Staff member not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Duplicate staff code, employee code or email"
            )
    })
    @PutMapping(
            value = "/{staffId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<StaffResponse> updateStaff(
            @PathVariable Long organizationId,
            @PathVariable Long staffId,
            @Valid @RequestPart("data") StaffUpdateRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        request.setStatus(StaffStatus.ACTIVE);
        StaffResponse response =
                staffService.updateStaff(
                        organizationId,
                        staffId,
                        request,
                        photo
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get staff member by ID",
            description = "Returns one staff member belonging to the organization"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Staff member found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Staff member not found"
            )
    })
    @GetMapping("/{staffId}")
    public ResponseEntity<StaffResponse> getStaffById(
            @PathVariable Long organizationId,
            @PathVariable Long staffId
    ) {
        StaffResponse response =
                staffService.getStaffById(
                        organizationId,
                        staffId
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get staff directory",
            description = """
                    Returns paginated staff members.
                    Supports keyword search, status filtering and active filtering.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Staff directory retrieved successfully"
            )
    })
    @GetMapping
    public ResponseEntity<StaffPageResponse> getStaffPage(
            @PathVariable Long organizationId,

            @Parameter(
                    description = "Page number, starting from 0",
                    example = "0"
            )
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    description = "Number of records per page",
                    example = "10"
            )
            @RequestParam(defaultValue = "10") int size,

            @Parameter(
                    description = "Search by staff code, name, email, phone or designation",
                    example = "teacher"
            )
            @RequestParam(required = false) String keyword,

            @Parameter(
                    description = "Filter by staff status",
                    example = "ACTIVE"
            )
            @RequestParam(required = false) StaffStatus status,

            @Parameter(
                    description = "Filter by active flag",
                    example = "true"
            )
            @RequestParam(required = false) Boolean active
    ) {
        StaffPageResponse response =
                staffService.getStaffPage(
                        organizationId,
                        page,
                        size,
                        keyword,
                        status,
                        active
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Activate staff member",
            description = "Activates a staff member and sets status to ACTIVE"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Staff member activated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Staff member not found"
            )
    })
    @PatchMapping("/{staffId}/activate")
    public ResponseEntity<StaffResponse> activateStaff(
            @PathVariable Long organizationId,
            @PathVariable Long staffId
    ) {
        StaffResponse response =
                staffService.activateStaff(
                        organizationId,
                        staffId
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Deactivate staff member",
            description = "Deactivates a staff member and sets status to INACTIVE"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Staff member deactivated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Staff member not found"
            )
    })
    @PatchMapping("/{staffId}/deactivate")
    public ResponseEntity<StaffResponse> deactivateStaff(
            @PathVariable Long organizationId,
            @PathVariable Long staffId
    ) {
        StaffResponse response =
                staffService.deactivateStaff(
                        organizationId,
                        staffId
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete staff member",
            description = "Permanently deletes a staff member"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Staff member deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Staff member not found"
            )
    })
    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(
            @PathVariable Long organizationId,
            @PathVariable Long staffId
    ) {
        staffService.deleteStaff(
                organizationId,
                staffId
        );

        return ResponseEntity.noContent().build();
    }
}