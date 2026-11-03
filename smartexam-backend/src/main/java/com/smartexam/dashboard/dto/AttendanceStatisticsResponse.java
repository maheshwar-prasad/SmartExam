package com.smartexam.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttendanceStatisticsResponse {

    private long totalRecords;

    private long present;

    private long absent;

    private long late;

    private double attendancePercentage;
}