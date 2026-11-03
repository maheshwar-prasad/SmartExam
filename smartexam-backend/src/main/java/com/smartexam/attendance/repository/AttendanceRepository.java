package com.smartexam.attendance.repository;

import com.smartexam.attendance.entity.Attendance;
import com.smartexam.common.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends
        JpaRepository<Attendance, Long>,
        JpaSpecificationExecutor<Attendance> {

    /**
     * Duplicate Attendance Validation
     */
    boolean existsByAttendanceDateAndStudentEnrollmentIdAndTeacherSubjectAssignmentId(
            LocalDate attendanceDate,
            Long studentEnrollmentId,
            Long teacherSubjectAssignmentId);

    /**
     * Duplicate Attendance Validation (Update)
     */
    boolean existsByAttendanceDateAndStudentEnrollmentIdAndTeacherSubjectAssignmentIdAndIdNot(
            LocalDate attendanceDate,
            Long studentEnrollmentId,
            Long teacherSubjectAssignmentId,
            Long id);

    /**
     * Active Attendance
     */
    Optional<Attendance> findByIdAndActiveTrue(
            Long id);

    /**
     * Student Attendance
     */
    List<Attendance> findByStudentEnrollmentIdAndActiveTrue(
            Long studentEnrollmentId);

    /**
     * Teacher Attendance
     */
    List<Attendance> findByTeacherSubjectAssignmentIdAndActiveTrue(
            Long teacherSubjectAssignmentId);

    /**
     * Attendance By Date
     */
    List<Attendance> findByAttendanceDateAndActiveTrue(
            LocalDate attendanceDate);

    /**
     * Attendance By Status
     */
    List<Attendance> findByStatusAndActiveTrue(
            com.smartexam.common.enums.AttendanceStatus status);

    /**
     * Student Attendance Between Dates
     */
    List<Attendance> findByStudentEnrollmentIdAndAttendanceDateBetweenAndActiveTrue(
            Long studentEnrollmentId,
            LocalDate fromDate,
            LocalDate toDate);

    /**
     * Attendance Between Dates
     */
    List<Attendance> findByAttendanceDateBetweenAndActiveTrue(
            LocalDate fromDate,
            LocalDate toDate);

    List<Attendance> findByAttendanceDateAndStudentEnrollmentClassRoomIdAndStudentEnrollmentSectionIdAndActiveTrue(
            LocalDate attendanceDate,
            Long classRoomId,
            Long sectionId);

    List<Attendance> findByTeacherSubjectAssignmentTeacherIdAndActiveTrue(
            Long teacherId);

    List<Attendance>
    findByTeacherSubjectAssignmentTeacherIdAndAttendanceDateBetweenAndActiveTrue(
            Long teacherId,
            LocalDate fromDate,
            LocalDate toDate);

    long countByActiveTrue();

    long countByActiveTrueAndStatus(
            AttendanceStatus status);
}