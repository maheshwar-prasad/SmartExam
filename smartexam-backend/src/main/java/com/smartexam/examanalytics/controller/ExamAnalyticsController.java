package com.smartexam.examanalytics.controller;

import com.smartexam.examanalytics.dto.ExamAnalyticsResponse;
import com.smartexam.examanalytics.service.ExamAnalyticsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam-analytics")
@Validated
public class ExamAnalyticsController {

    private final ExamAnalyticsService examAnalyticsService;

    public ExamAnalyticsController(
            ExamAnalyticsService examAnalyticsService) {

        this.examAnalyticsService =
                examAnalyticsService;
    }

    /**
     * Get complete analytics for an exam.
     */
    @GetMapping("/exam/{examId}")
    public ResponseEntity<ExamAnalyticsResponse>
    getExamAnalytics(
            @PathVariable Long examId) {

        return ResponseEntity.ok(
                examAnalyticsService
                        .getExamAnalytics(examId)
        );
    }

    /**
     * Get summary statistics for an exam.
     */
    @GetMapping("/exam/{examId}/summary")
    public ResponseEntity<ExamAnalyticsResponse>
    getExamSummary(
            @PathVariable Long examId) {

        return ResponseEntity.ok(
                examAnalyticsService
                        .getExamSummary(examId)
        );
    }

    /**
     * Get grade-wise distribution.
     */
    @GetMapping("/exam/{examId}/grades")
    public ResponseEntity<
            List<ExamAnalyticsResponse.GradeDistribution>>
    getGradeDistribution(
            @PathVariable Long examId) {

        return ResponseEntity.ok(
                examAnalyticsService
                        .getGradeDistribution(examId)
        );
    }

    /**
     * Get top-performing students.
     */

    @GetMapping("/exam/{examId}/top-students")
    public ResponseEntity<
            List<ExamAnalyticsResponse.TopStudent>>
    getTopStudents(
            @PathVariable Long examId,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Limit must be at least 1")
            @Max(value = 100, message = "Limit cannot exceed 100")
            int limit) {

        return ResponseEntity.ok(
                examAnalyticsService
                        .getTopStudents(
                                examId,
                                limit
                        )
        );
    }
}