package com.smartexam.staff.leave.controller;
import com.smartexam.staff.leave.dto.LeaveBalanceRequest;
import com.smartexam.staff.leave.dto.LeaveBalanceResponse;
import com.smartexam.staff.leave.dto.LeaveBalanceUpdateRequest;
import com.smartexam.staff.leave.service.LeaveBalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leave-balances")
@RequiredArgsConstructor
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    @PostMapping
    public ResponseEntity<LeaveBalanceResponse> create(
            @Valid @RequestBody LeaveBalanceRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(leaveBalanceService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveBalanceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LeaveBalanceUpdateRequest request
    ) {
        return ResponseEntity.ok(
                leaveBalanceService.update(id, request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveBalanceResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                leaveBalanceService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<LeaveBalanceResponse>> getAll() {
        return ResponseEntity.ok(
                leaveBalanceService.getAll()
        );
    }

    @GetMapping("/page")
    public ResponseEntity<Page<LeaveBalanceResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        return ResponseEntity.ok(
                leaveBalanceService.getAll(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<LeaveBalanceResponse>> getByOrganization(
            @PathVariable Long organizationId
    ) {
        return ResponseEntity.ok(
                leaveBalanceService.getByOrganization(
                        organizationId
                )
        );
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<LeaveBalanceResponse>> getByTeacher(
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(
                leaveBalanceService.getByTeacher(teacherId)
        );
    }

    @GetMapping("/teacher/{teacherId}/session/{academicSessionId}")
    public ResponseEntity<List<LeaveBalanceResponse>>
    getByTeacherAndSession(
            @PathVariable Long teacherId,
            @PathVariable Long academicSessionId
    ) {
        return ResponseEntity.ok(
                leaveBalanceService.getByTeacherAndSession(
                        teacherId,
                        academicSessionId
                )
        );
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<LeaveBalanceResponse> activate(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                leaveBalanceService.activate(id)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<LeaveBalanceResponse> deactivate(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                leaveBalanceService.deactivate(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        leaveBalanceService.delete(id);

        return ResponseEntity.noContent().build();
    }
}