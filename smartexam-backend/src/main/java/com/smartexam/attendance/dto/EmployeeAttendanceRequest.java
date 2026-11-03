package com.smartexam.attendance.dto;

import com.smartexam.attendance.enums.EmployeeAttendanceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class EmployeeAttendanceRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotNull(message = "Attendance date is required")
    private LocalDate attendanceDate;

    @NotNull(message = "Attendance status is required")
    private EmployeeAttendanceStatus status;

    private LocalTime checkInTime;

    private LocalTime checkOutTime;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;
}