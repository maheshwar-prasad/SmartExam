package com.smartexam.staff.leave.repository;

import com.smartexam.staff.leave.entity.LeaveRequest;
import com.smartexam.staff.leave.enums.LeaveRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository
        extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByOrganizationId(
            Long organizationId
    );

    List<LeaveRequest> findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    List<LeaveRequest> findByTeacherId(
            Long teacherId
    );

    List<LeaveRequest> findByTeacherIdAndAcademicSessionId(
            Long teacherId,
            Long academicSessionId
    );

    List<LeaveRequest> findByTeacherIdAndStatus(
            Long teacherId,
            LeaveRequestStatus status
    );

    List<LeaveRequest> findByOrganizationIdAndStatus(
            Long organizationId,
            LeaveRequestStatus status
    );

    List<LeaveRequest> findByOrganizationIdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            Long organizationId,
            LocalDate toDate,
            LocalDate fromDate
    );

    boolean existsByTeacherIdAndStatusAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            Long teacherId,
            LeaveRequestStatus status,
            LocalDate toDate,
            LocalDate fromDate
    );
}