package com.smartexam.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeMonthlyAttendanceResponse {

    private Long employeeId;
    private String employeeCode;
    private String employeeName;

    private long presentDays;
    private long absentDays;
    private long halfDays;
    private long leaveDays;
    private long holidays;
    private long weeklyOffs;

    private long totalRecordedDays;
    private double attendancePercentage;
}