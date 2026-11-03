package com.smartexam.attendance.service;

import com.smartexam.attendance.dto.*;
import com.smartexam.attendance.enums.EmployeeAttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeAttendanceService {

    EmployeeAttendanceResponse markAttendance(
            EmployeeAttendanceRequest request);

    EmployeeAttendanceResponse updateAttendance(
            Long attendanceId,
            EmployeeAttendanceRequest request);

    EmployeeAttendanceResponse getAttendanceById(
            Long attendanceId);

    Page<EmployeeAttendanceResponse> getDailyAttendance(
            Long organizationId,
            LocalDate attendanceDate,
            Pageable pageable);

    List<EmployeeAttendanceResponse> getAttendanceBetweenDates(
            Long organizationId,
            LocalDate startDate,
            LocalDate endDate);

    List<EmployeeAttendanceResponse> getEmployeeAttendanceBetweenDates(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate);

    long countByStatus(
            Long organizationId,
            LocalDate attendanceDate,
            EmployeeAttendanceStatus status);
    List<EmployeeMonthlyAttendanceResponse> getMonthlyAttendanceSummary(
            Long organizationId,
            LocalDate startDate,
            LocalDate endDate);

    EmployeeAttendanceResponse checkIn(
            Long employeeId,
            Long organizationId,
            LocalDate attendanceDate,
            java.time.LocalTime checkInTime);

    EmployeeAttendanceResponse checkOut(
            Long employeeId,
            Long organizationId,
            LocalDate attendanceDate,
            java.time.LocalTime checkOutTime);

    EmployeeAttendanceDashboardResponse getDashboard(
            Long organizationId,
            LocalDate attendanceDate);

    BulkEmployeeAttendanceResponse markBulkAttendance(
            BulkEmployeeAttendanceRequest request);
}