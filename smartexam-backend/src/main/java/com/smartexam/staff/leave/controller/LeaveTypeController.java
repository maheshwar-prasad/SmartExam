package com.smartexam.staff.leave.controller;
import com.smartexam.staff.leave.dto.LeaveTypeRequest;
import com.smartexam.staff.leave.dto.LeaveTypeResponse;
import com.smartexam.staff.leave.dto.LeaveTypeUpdateRequest;
import com.smartexam.staff.leave.service.LeaveTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leave-types")
@RequiredArgsConstructor
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @PostMapping
    public ResponseEntity<LeaveTypeResponse> create(
            @Valid @RequestBody LeaveTypeRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(leaveTypeService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LeaveTypeUpdateRequest request
    ) {
        return ResponseEntity.ok(
                leaveTypeService.update(id, request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveTypeResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                leaveTypeService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<LeaveTypeResponse>> getAll() {
        return ResponseEntity.ok(
                leaveTypeService.getAll()
        );
    }

    @GetMapping("/page")
    public ResponseEntity<Page<LeaveTypeResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        return ResponseEntity.ok(
                leaveTypeService.getAll(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<LeaveTypeResponse>> getByOrganization(
            @PathVariable Long organizationId
    ) {
        return ResponseEntity.ok(
                leaveTypeService.getByOrganization(
                        organizationId
                )
        );
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<LeaveTypeResponse> activate(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                leaveTypeService.activate(id)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<LeaveTypeResponse> deactivate(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                leaveTypeService.deactivate(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        leaveTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}