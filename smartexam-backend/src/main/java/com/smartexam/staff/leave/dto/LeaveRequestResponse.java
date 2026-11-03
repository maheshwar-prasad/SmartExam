package com.smartexam.staff.leave.dto;
import com.smartexam.staff.leave.enums.LeaveRequestStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeaveRequestResponse(

        Long id,

        Long organizationId,
        String organizationName,

        Long teacherId,
        String teacherName,
        String teacherEmployeeCode,

        Long leaveTypeId,
        String leaveTypeName,
        String leaveTypeCode,

        Long academicSessionId,
        String academicSessionName,

        LocalDate fromDate,
        LocalDate toDate,
        BigDecimal numberOfDays,

        String reason,

        LeaveRequestStatus status,

        LocalDateTime appliedAt,

        Long approvedBy,
        LocalDateTime approvedAt,

        Long rejectedBy,
        LocalDateTime rejectedAt,

        String approvalComments,

        Boolean active,

        LocalDateTime createdAt,
        Long createdBy,

        LocalDateTime updatedAt,
        Long updatedBy
) {
}