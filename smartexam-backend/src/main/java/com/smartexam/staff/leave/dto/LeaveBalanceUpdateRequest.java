package com.smartexam.staff.leave.dto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record LeaveBalanceUpdateRequest(

        Long organizationId,

        Long teacherId,

        Long leaveTypeId,

        Long academicSessionId,

        @DecimalMin(
                value = "0.0",
                message = "Allocated days cannot be negative"
        )
        BigDecimal allocatedDays,

        @DecimalMin(
                value = "0.0",
                message = "Used days cannot be negative"
        )
        BigDecimal usedDays,

        @DecimalMin(
                value = "0.0",
                message = "Pending days cannot be negative"
        )
        BigDecimal pendingDays,

        @DecimalMin(
                value = "0.0",
                message = "Remaining days cannot be negative"
        )
        BigDecimal remainingDays,

        Boolean active
) {
}