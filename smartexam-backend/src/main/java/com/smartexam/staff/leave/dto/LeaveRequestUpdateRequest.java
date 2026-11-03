package com.smartexam.staff.leave.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record LeaveRequestUpdateRequest(

        Long organizationId,

        Long teacherId,

        Long leaveTypeId,

        Long academicSessionId,

        LocalDate fromDate,

        LocalDate toDate,

        @Size(
                max = 1000,
                message = "Reason must not exceed 1000 characters"
        )
        String reason
) {
}