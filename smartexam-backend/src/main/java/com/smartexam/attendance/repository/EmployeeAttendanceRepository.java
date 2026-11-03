package com.smartexam.attendance.repository;

import com.smartexam.attendance.entity.EmployeeAttendance;
import com.smartexam.attendance.enums.EmployeeAttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeAttendanceRepository
        extends JpaRepository<EmployeeAttendance, Long> {

    Optional<EmployeeAttendance> findByEmployee_IdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate
    );

    boolean existsByEmployee_IdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate
    );

    Page<EmployeeAttendance> findByOrganization_IdAndAttendanceDate(
            Long organizationId,
            LocalDate attendanceDate,
            Pageable pageable
    );

    List<EmployeeAttendance> findByOrganization_IdAndAttendanceDateBetween(
            Long organizationId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<EmployeeAttendance> findByEmployee_IdAndAttendanceDateBetween(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    );
    long countByOrganization_IdAndAttendanceDateAndStatus(
            Long organizationId,
            LocalDate attendanceDate,
            EmployeeAttendanceStatus status);

    @Query("""
    SELECT ea.employee.id, ea.status, COUNT(ea.id)
    FROM EmployeeAttendance ea
    WHERE ea.organization.id = :organizationId
      AND ea.attendanceDate BETWEEN :startDate AND :endDate
    GROUP BY ea.employee.id, ea.status
    """)
    List<Object[]> getMonthlyAttendanceCounts(
            @Param("organizationId") Long organizationId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}