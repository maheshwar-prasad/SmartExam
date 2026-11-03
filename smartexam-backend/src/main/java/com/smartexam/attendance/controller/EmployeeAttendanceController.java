package com.smartexam.attendance.controller;

import com.smartexam.attendance.dto.*;
import com.smartexam.attendance.enums.EmployeeAttendanceStatus;
import com.smartexam.attendance.service.EmployeeAttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employee-attendance")
@RequiredArgsConstructor
public class EmployeeAttendanceController {

    private final EmployeeAttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<EmployeeAttendanceResponse> markAttendance(
            @Valid @RequestBody EmployeeAttendanceRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceService.markAttendance(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeAttendanceResponse> updateAttendance(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeAttendanceRequest request) {

        return ResponseEntity.ok(
                attendanceService.updateAttendance(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeAttendanceResponse> getAttendanceById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceById(id));
    }

    @GetMapping("/daily")
    public ResponseEntity<Page<EmployeeAttendanceResponse>> getDailyAttendance(
            @RequestParam Long organizationId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate attendanceDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "employeeCode") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        if (page < 0 || size < 1 || size > 200) {
            throw new IllegalArgumentException(
                    "Page must be non-negative and size must be between 1 and 200");
        }

        String sortProperty = switch (sortBy) {
            case "employeeCode" -> "employee.employeeCode";
            case "attendanceDate" -> "attendanceDate";
            case "status" -> "status";
            default -> throw new IllegalArgumentException(
                    "Unsupported sort field: " + sortBy);
        };

        Sort.Direction direction;

        try {
            direction = Sort.Direction.fromString(sortDirection);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "sortDirection must be ASC or DESC");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortProperty)
        );
        return ResponseEntity.ok(
                attendanceService.getDailyAttendance(
                        organizationId, attendanceDate, pageable));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<EmployeeAttendanceResponse>>
    getAttendanceBetweenDates(
            @RequestParam Long organizationId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceBetweenDates(
                        organizationId, startDate, endDate));
    }
    @GetMapping("/employee/{employeeId}/date-range")
    public ResponseEntity<List<EmployeeAttendanceResponse>>
    getEmployeeAttendance(
            @PathVariable Long employeeId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return ResponseEntity.ok(
                attendanceService.getEmployeeAttendanceBetweenDates(
                        employeeId, startDate, endDate));
    }
    @GetMapping("/daily/count")
    public ResponseEntity<Map<String, Long>> getDailyStatusCounts(
            @RequestParam Long organizationId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate attendanceDate) {

        Map<String, Long> counts = new java.util.LinkedHashMap<>();
        for (EmployeeAttendanceStatus status
                : EmployeeAttendanceStatus.values()) {
            counts.put(
                    status.name(),
                    attendanceService.countByStatus(
                            organizationId, attendanceDate, status));
        }
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<List<EmployeeMonthlyAttendanceResponse>>
    getMonthlyAttendanceSummary(
            @RequestParam Long organizationId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return ResponseEntity.ok(
                attendanceService.getMonthlyAttendanceSummary(
                        organizationId, startDate, endDate));
    }

    @PostMapping("/check-in")
    public ResponseEntity<EmployeeAttendanceResponse> checkIn(
            @RequestParam Long employeeId,
            @RequestParam Long organizationId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate attendanceDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            java.time.LocalTime checkInTime) {

        return ResponseEntity.ok(
                attendanceService.checkIn(
                        employeeId, organizationId,
                        attendanceDate, checkInTime));
    }

    @PostMapping("/check-out")
    public ResponseEntity<EmployeeAttendanceResponse> checkOut(
            @RequestParam Long employeeId,
            @RequestParam Long organizationId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate attendanceDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            java.time.LocalTime checkOutTime) {

        return ResponseEntity.ok(
                attendanceService.checkOut(
                        employeeId, organizationId,
                        attendanceDate, checkOutTime));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<EmployeeAttendanceDashboardResponse> getDashboard(
            @RequestParam Long organizationId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate attendanceDate) {

        return ResponseEntity.ok(
                attendanceService.getDashboard(
                        organizationId, attendanceDate));
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkEmployeeAttendanceResponse> markBulkAttendance(
            @Valid @RequestBody BulkEmployeeAttendanceRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceService.markBulkAttendance(request));
    }

    @PatchMapping("/{id}/correction")
    public ResponseEntity<EmployeeAttendanceResponse> correctAttendance(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeAttendanceRequest request) {

        return ResponseEntity.ok(
                attendanceService.updateAttendance(id, request));
    }
}