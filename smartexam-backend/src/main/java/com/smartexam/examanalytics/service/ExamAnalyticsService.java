package com.smartexam.examanalytics.service;

import com.smartexam.examanalytics.dto.ExamAnalyticsResponse;

import java.util.List;

public interface ExamAnalyticsService {

    /**
     * Get complete analytics for an exam.
     */
    ExamAnalyticsResponse getExamAnalytics(
            Long examId
    );

    /**
     * Get summary statistics for an exam.
     */
    ExamAnalyticsResponse getExamSummary(
            Long examId
    );

    /**
     * Get grade-wise distribution.
     */
    List<ExamAnalyticsResponse.GradeDistribution>
    getGradeDistribution(
            Long examId
    );

    /**
     * Get top-performing students.
     *
     * @param examId exam ID
     * @param limit maximum number of students
     */
    List<ExamAnalyticsResponse.TopStudent>
    getTopStudents(
            Long examId,
            int limit
    );
}