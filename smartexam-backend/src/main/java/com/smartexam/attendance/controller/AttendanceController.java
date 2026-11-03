package com.smartexam.attendance.controller;

import com.smartexam.attendance.dto.*;
import com.smartexam.attendance.service.AttendanceService;
import com.smartexam.common.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@Validated
@Tag(name = "Attendance", description = "Attendance Management APIs")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "Create Attendance")
    public ResponseEntity<AttendanceResponse> create(
            @Valid @RequestBody AttendanceRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Attendance")
    public ResponseEntity<AttendanceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceUpdateRequest request) {

        return ResponseEntity.ok(
                attendanceService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Attendance By Id")
    public ResponseEntity<AttendanceResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                attendanceService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get All Attendance")
    public ResponseEntity<List<AttendanceResponse>> getAll() {

        return ResponseEntity.ok(
                attendanceService.getAll());
    }

    @GetMapping("/page")
    @Operation(summary = "Get Attendance With Pagination")
    public ResponseEntity<Page<AttendanceResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        return ResponseEntity.ok(
                attendanceService.getAll(page, size, sortBy, direction));
    }

    @PostMapping("/search")
    @Operation(summary = "Search Attendance")
    public ResponseEntity<Page<AttendanceResponse>> search(
            @RequestBody AttendanceSearchRequest request) {

        return ResponseEntity.ok(
                attendanceService.search(request));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Attendance")
    public ResponseEntity<AttendanceResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                attendanceService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Attendance")
    public ResponseEntity<AttendanceResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                attendanceService.deactivate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Attendance")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        attendanceService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    @Operation(summary = "Mark Bulk Attendance")
    public ResponseEntity<List<AttendanceResponse>> markBulkAttendance(
            @Valid @RequestBody BulkAttendanceRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceService.markBulkAttendance(request));
    }

    @GetMapping("/daily")
    @Operation(summary = "Get Daily Attendance")
    public ResponseEntity<List<AttendanceResponse>> getDailyAttendance(
            @RequestParam LocalDate attendanceDate,
            @RequestParam Long classRoomId,
            @RequestParam Long sectionId) {

        return ResponseEntity.ok(
                attendanceService.getDailyAttendance(
                        attendanceDate,
                        classRoomId,
                        sectionId));
    }

    @GetMapping("/student/{studentEnrollmentId}/monthly-summary")
    @Operation(summary = "Get Monthly Attendance Summary")
    public ResponseEntity<MonthlyAttendanceSummaryResponse>
    getMonthlyAttendanceSummary(
            @PathVariable Long studentEnrollmentId,
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(
                attendanceService.getMonthlyAttendanceSummary(
                        studentEnrollmentId,
                        year,
                        month));
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get Teacher Attendance")
    public ResponseEntity<List<AttendanceResponse>> getTeacherAttendance(
            @PathVariable Long teacherId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate) {

        boolean hasFromDate = fromDate != null;
        boolean hasToDate = toDate != null;

        if (hasFromDate != hasToDate) {
            throw new BusinessException(
                    "Both fromDate and toDate must be provided together.");
        }

        if (hasFromDate) {
            return ResponseEntity.ok(
                    attendanceService.getTeacherAttendance(
                            teacherId,
                            fromDate,
                            toDate));
        }

        return ResponseEntity.ok(
                attendanceService.getTeacherAttendance(teacherId));
    }

}