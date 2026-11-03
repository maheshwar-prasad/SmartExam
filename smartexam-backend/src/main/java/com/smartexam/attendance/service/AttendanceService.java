package com.smartexam.attendance.service;

import com.smartexam.attendance.dto.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    AttendanceResponse create(
            AttendanceRequest request);

    AttendanceResponse update(
            Long id,
            AttendanceUpdateRequest request);

    AttendanceResponse getById(
            Long id);

    List<AttendanceResponse> getAll();

    Page<AttendanceResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction);

    Page<AttendanceResponse> search(
            AttendanceSearchRequest request);

    AttendanceResponse activate(
            Long id);

    AttendanceResponse deactivate(
            Long id);

    void delete(
            Long id);

    List<AttendanceResponse> markBulkAttendance(BulkAttendanceRequest request);

    List<AttendanceResponse> getDailyAttendance(
            LocalDate attendanceDate,
            Long classRoomId,
            Long sectionId);

    MonthlyAttendanceSummaryResponse getMonthlyAttendanceSummary(
            Long studentEnrollmentId,
            int year,
            int month);

    List<AttendanceResponse> getTeacherAttendance(
            Long teacherId);

    List<AttendanceResponse> getTeacherAttendance(
            Long teacherId,
            LocalDate fromDate,
            LocalDate toDate);
}