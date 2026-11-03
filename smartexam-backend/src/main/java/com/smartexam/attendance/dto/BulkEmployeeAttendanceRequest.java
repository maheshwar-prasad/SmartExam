package com.smartexam.attendance.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BulkEmployeeAttendanceRequest {

    @NotNull(message = "Organization ID is required")
    @Positive(message = "Organization ID must be positive")
    private Long organizationId;

    @NotNull(message = "Attendance date is required")
    private LocalDate attendanceDate;

    @NotEmpty(message = "Attendance records are required")
    @Valid
    private List<EmployeeAttendanceRequest> records;
}