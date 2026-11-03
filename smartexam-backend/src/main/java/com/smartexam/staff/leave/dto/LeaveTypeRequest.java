package com.smartexam.staff.leave.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record LeaveTypeRequest(

        @NotNull(message = "Organization is required")
        Long organizationId,

        @NotBlank(message = "Leave type name is required")
        @Size(max = 100, message = "Leave type name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Leave type code is required")
        @Size(max = 50, message = "Leave type code must not exceed 50 characters")
        String code,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @NotNull(message = "Days per year is required")
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