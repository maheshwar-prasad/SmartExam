package com.smartexam.staff.leave.repository;
import com.smartexam.staff.leave.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository
        extends JpaRepository<LeaveBalance, Long> {

    List<LeaveBalance> findByOrganizationId(
            Long organizationId
    );

    List<LeaveBalance> findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    List<LeaveBalance> findByTeacherId(
            Long teacherId
    );

    List<LeaveBalance> findByTeacherIdAndAcademicSessionId(
            Long teacherId,
            Long academicSessionId
    );

    List<LeaveBalance> findByTeacherIdAndLeaveTypeId(
            Long teacherId,
            Long leaveTypeId
    );

    Optional<LeaveBalance> findByIdAndOrganizationId(
            Long id,
            Long organizationId
    );

    Optional<LeaveBalance>
    findByOrganizationIdAndTeacherIdAndLeaveTypeIdAndAcademicSessionId(
            Long organizationId,
            Long teacherId,
            Long leaveTypeId,
            Long academicSessionId
    );

    boolean existsByOrganizationIdAndTeacherIdAndLeaveTypeIdAndAcademicSessionId(
            Long organizationId,
            Long teacherId,
            Long leaveTypeId,
            Long academicSessionId
    );

    boolean existsByOrganizationIdAndTeacherIdAndLeaveTypeIdAndAcademicSessionIdAndIdNot(
            Long organizationId,
            Long teacherId,
            Long leaveTypeId,
            Long academicSessionId,
            Long id
    );
}