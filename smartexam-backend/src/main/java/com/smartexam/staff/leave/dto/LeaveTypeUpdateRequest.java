package com.smartexam.staff.leave.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record LeaveTypeUpdateRequest(

        Long organizationId,

        @Size(
                max = 100,
                message = "Leave type name must not exceed 100 characters"
        )
        String name,

        @Size(
                max = 50,
                message = "Leave type code must not exceed 50 characters"
        )
        String code,

        @Size(
                max = 500,
                message = "Description must not exceed 500 characters"
        )
        String description,

        @DecimalMin(
                value = "0.0",
                inclusive = true,
                message = "Days per year cannot be negative"
        )
        BigDecimal daysPerYear,

        Boolean carryForwardAllowed,

        @DecimalMin(
                value = "0.0",
                inclusive = true,
                message = "Maximum carry forward days cannot be negative"
        )
        BigDecimal maxCarryForwardDays,

        Boolean requiresApproval,

        Boolean active
) {
}