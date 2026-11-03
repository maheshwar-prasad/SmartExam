package com.smartexam.dashboard.service;

import com.smartexam.dashboard.dto.AttendanceStatisticsResponse;
import com.smartexam.dashboard.dto.DashboardFilterRequest;
import com.smartexam.dashboard.dto.DashboardSummaryResponse;
import com.smartexam.dashboard.dto.ExamStatisticsResponse;
import com.smartexam.dashboard.dto.ResultStatisticsResponse;
import com.smartexam.dashboard.dto.StudentStatisticsResponse;

public interface DashboardService {

    DashboardSummaryResponse getSummary(
            DashboardFilterRequest filter
    );

    StudentStatisticsResponse getStudentStatistics();

    AttendanceStatisticsResponse getAttendanceStatistics();

    ExamStatisticsResponse getExamStatistics();

    ResultStatisticsResponse getResultStatistics();
}