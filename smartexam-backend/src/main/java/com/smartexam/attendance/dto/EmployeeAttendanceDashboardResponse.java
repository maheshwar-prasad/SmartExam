package com.smartexam.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeAttendanceDashboardResponse {

    private Long organizationId;

    private long totalActiveEmployees;
    private long totalRecordedEmployees;

    private long present;
    private long absent;
    private long halfDay;
    private long onLeave;
    private long holidays;
    private long weeklyOff;

    private long notMarked;

    private double attendancePercentage;
}