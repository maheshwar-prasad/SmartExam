package com.smartexam.attendance.dto;

import com.smartexam.attendance.enums.EmployeeAttendanceStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class EmployeeAttendanceResponse {

    private Long id;

    private Long employeeId;
    private Long organizationId;

    private String employeeCode;
    private String employeeName;
    private String employeeType;

    private LocalDate attendanceDate;
    private EmployeeAttendanceStatus status;

    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}