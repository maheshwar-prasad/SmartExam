package com.smartexam.staff.leave.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record LeaveBalanceRequest(

        @NotNull(message = "Organization is required")
        Long organizationId,

        @NotNull(message = "Teacher is required")
        Long teacherId,

        @NotNull(message = "Leave type is required")
        Long leaveTypeId,

        @NotNull(message = "Academic session is required")
        Long academicSessionId,

        @NotNull(message = "Allocated days are required")
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