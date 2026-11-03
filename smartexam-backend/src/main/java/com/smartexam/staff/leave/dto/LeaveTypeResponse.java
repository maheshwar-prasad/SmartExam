package com.smartexam.staff.leave.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LeaveTypeResponse(

        Long id,

        Long organizationId,

        String organizationName,

        String name,

        String code,

        String description,

        BigDecimal daysPerYear,

        Boolean carryForwardAllowed,

        BigDecimal maxCarryForwardDays,

        Boolean requiresApproval,

        Boolean active,

        LocalDateTime createdAt,

        Long createdBy,

        LocalDateTime updatedAt,

        Long updatedBy
) {
}