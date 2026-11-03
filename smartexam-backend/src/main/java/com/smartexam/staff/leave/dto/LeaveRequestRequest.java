package com.smartexam.staff.leave.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record LeaveRequestRequest(

        @NotNull(message = "Organization is required")
        Long organizationId,

        @NotNull(message = "Teacher is required")
        Long teacherId,

        @NotNull(message = "Leave type is required")
        Long leaveTypeId,

        @NotNull(message = "Academic session is required")
        Long academicSessionId,

        @NotNull(message = "From date is required")
        LocalDate fromDate,

        @NotNull(message = "To date is required")
        LocalDate toDate,

        @NotBlank(message = "Reason is required")
        @Size(
                max = 1000,
                message = "Reason must not exceed 1000 characters"
        )
        String reason
) {
}