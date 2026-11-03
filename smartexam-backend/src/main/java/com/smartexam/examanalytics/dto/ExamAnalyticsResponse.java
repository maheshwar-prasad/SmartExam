package com.smartexam.examanalytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamAnalyticsResponse {

    private Long examId;

    private String examName;

    private Long organizationId;

    // Student Statistics
    private Integer totalStudents;

    private Integer totalResults;

    private Integer passedStudents;

    private Integer failedStudents;

    private Integer incompleteStudents;

    // Performance Statistics
    private BigDecimal passPercentage;

    private BigDecimal averagePercentage;

    private BigDecimal highestPercentage;

    private BigDecimal lowestPercentage;

    // Grade Distribution
    private List<GradeDistribution> gradeDistribution;

    // Top Students
    private List<TopStudent> topStudents;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GradeDistribution {

        private String grade;

        private Long studentCount;

        private BigDecimal percentage;
    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopStudent {

        private Long studentId;

        private String studentName;

        private String rollNumber;

        private BigDecimal totalObtainedMarks;

        private BigDecimal totalMaximumMarks;

        private BigDecimal percentage;

        private String grade;

        private String resultStatus;
    }
}