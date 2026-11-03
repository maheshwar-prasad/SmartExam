package com.smartexam.staff.leave.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LeaveBalanceResponse(

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

        BigDecimal allocatedDays,
        BigDecimal usedDays,
        BigDecimal pendingDays,
        BigDecimal remainingDays,

        Boolean active,

        LocalDateTime createdAt,
        Long createdBy,

        LocalDateTime updatedAt,
        Long updatedBy
) {
}