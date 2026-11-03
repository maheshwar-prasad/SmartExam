package com.smartexam.staff.leave.controller;

import com.smartexam.staff.leave.dto.LeaveRequestRequest;
import com.smartexam.staff.leave.dto.LeaveRequestResponse;
import com.smartexam.staff.leave.dto.LeaveRequestUpdateRequest;
import com.smartexam.staff.leave.enums.LeaveRequestStatus;
import com.smartexam.staff.leave.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;


    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<LeaveRequestResponse> create(
            @Valid @RequestBody LeaveRequestRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        leaveRequestService.create(request)
                );
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequestResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestUpdateRequest request
    ) {

        return ResponseEntity.ok(
                leaveRequestService.update(
                        id,
                        request
                )
        );
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                leaveRequestService.getById(id)
        );
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    public ResponseEntity<List<LeaveRequestResponse>> getAll() {

        return ResponseEntity.ok(
                leaveRequestService.getAll()
        );
    }


    // =========================================================
    // PAGINATION
    // =========================================================

    @GetMapping("/page")
    public ResponseEntity<Page<LeaveRequestResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(
                    defaultValue = "appliedAt"
            ) String sortBy,
            @RequestParam(
                    defaultValue = "DESC"
            ) String direction
    ) {

        return ResponseEntity.ok(
                leaveRequestService.getAll(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }


    // =========================================================
    // ORGANIZATION
    // =========================================================

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<LeaveRequestResponse>>
    getByOrganization(
            @PathVariable Long organizationId
    ) {

        return ResponseEntity.ok(
                leaveRequestService.getByOrganization(
                        organizationId
                )
        );
    }


    // =========================================================
    // TEACHER
    // =========================================================

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<LeaveRequestResponse>>
    getByTeacher(
            @PathVariable Long teacherId
    ) {

        return ResponseEntity.ok(
                leaveRequestService.getByTeacher(
                        teacherId
                )
        );
    }


    // =========================================================
    // TEACHER + ACADEMIC SESSION
    // =========================================================

    @GetMapping(
            "/teacher/{teacherId}/session/{academicSessionId}"
    )
    public ResponseEntity<List<LeaveRequestResponse>>
    getByTeacherAndSession(
            @PathVariable Long teacherId,
            @PathVariable Long academicSessionId
    ) {

        return ResponseEntity.ok(
                leaveRequestService.getByTeacherAndSession(
                        teacherId,
                        academicSessionId
                )
        );
    }


    // =========================================================
    // STATUS
    // =========================================================

    @GetMapping("/organization/{organizationId}/status/{status}")
    public ResponseEntity<List<LeaveRequestResponse>>
    getByStatus(
            @PathVariable Long organizationId,
            @PathVariable LeaveRequestStatus status
    ) {

        return ResponseEntity.ok(
                leaveRequestService.getByStatus(
                        organizationId,
                        status
                )
        );
    }


    // =========================================================
    // APPROVE
    // =========================================================

    @PatchMapping("/{id}/approve")
    public ResponseEntity<LeaveRequestResponse> approve(
            @PathVariable Long id,
            @RequestParam Long approvedBy,
            @RequestParam(required = false) String comments
    ) {

        return ResponseEntity.ok(
                leaveRequestService.approve(
                        id,
                        approvedBy,
                        comments
                )
        );
    }


    // =========================================================
    // REJECT
    // =========================================================

    @PatchMapping("/{id}/reject")
    public ResponseEntity<LeaveRequestResponse> reject(
            @PathVariable Long id,
            @RequestParam Long rejectedBy,
            @RequestParam(required = false) String comments
    ) {

        return ResponseEntity.ok(
                leaveRequestService.reject(
                        id,
                        rejectedBy,
                        comments
                )
        );
    }


    // =========================================================
    // CANCEL
    // =========================================================

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<LeaveRequestResponse> cancel(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                leaveRequestService.cancel(id)
        );
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<LeaveRequestResponse> activate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                leaveRequestService.activate(id)
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<LeaveRequestResponse> deactivate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                leaveRequestService.deactivate(id)
        );
    }


    // =========================================================
    // DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        leaveRequestService.delete(id);

        return ResponseEntity.noContent().build();
    }
}