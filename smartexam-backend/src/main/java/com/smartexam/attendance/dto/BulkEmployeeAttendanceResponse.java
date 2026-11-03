package com.smartexam.attendance.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BulkEmployeeAttendanceResponse {

    private int totalRequested;
    private int totalSaved;
    private List<EmployeeAttendanceResponse> records;
}