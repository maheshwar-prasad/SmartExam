package com.smartexam.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAttendanceSummaryResponse {

    private Long studentEnrollmentId;

    private int year;

    private int month;

    private long totalDays;

    private long presentDays;

    private long absentDays;

    private long lateDays;

    private long halfDays;

    private long leaveDays;

    private double attendancePercentage;
}