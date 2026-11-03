package com.smartexam.dashboard.controller;

import com.smartexam.dashboard.dto.*;
import com.smartexam.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary(
            @ModelAttribute DashboardFilterRequest filter) {

        return ResponseEntity.ok(
                dashboardService.getSummary(filter)
        );
    }

    @GetMapping("/student-statistics")
    public ResponseEntity<StudentStatisticsResponse>
    getStudentStatistics() {

        return ResponseEntity.ok(
                dashboardService.getStudentStatistics()
        );
    }

    @GetMapping("/attendance-statistics")
    public ResponseEntity<AttendanceStatisticsResponse>
    getAttendanceStatistics() {

        return ResponseEntity.ok(
                dashboardService.getAttendanceStatistics()
        );
    }

    @GetMapping("/exam-statistics")
    public ResponseEntity<ExamStatisticsResponse>
    getExamStatistics() {

        return ResponseEntity.ok(
                dashboardService.getExamStatistics()
        );
    }

    @GetMapping("/result-statistics")
    public ResponseEntity<ResultStatisticsResponse>
    getResultStatistics() {

        return ResponseEntity.ok(
                dashboardService.getResultStatistics()
        );
    }

}