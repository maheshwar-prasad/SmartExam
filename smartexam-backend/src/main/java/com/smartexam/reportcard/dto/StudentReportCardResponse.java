package com.smartexam.reportcard.dto;

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
public class StudentReportCardResponse {

    // Student Information
    private Long studentId;
    private String studentName;
    private String rollNumber;

    // Organization Information
    private Long organizationId;
    private String organizationName;

    // Exam Information
    private Long examId;
    private String examName;
    private String examType;

    // Class Information
    private Long academicSessionId;
    private String academicSessionName;

    private Long classRoomId;
    private String classRoomName;

    private Long sectionId;
    private String sectionName;

    // Subject-wise Results
    private List<SubjectResult> subjects;

    // Overall Result
    private Integer totalSubjects;

    private BigDecimal totalMaximumMarks;

    private BigDecimal totalObtainedMarks;

    private BigDecimal percentage;

    private String grade;

    private String resultStatus;

    private Boolean published;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectResult {

        private Long subjectId;

        private String subjectCode;

        private String subjectName;

        private BigDecimal maximumMarks;

        private BigDecimal passMarks;

        private BigDecimal obtainedMarks;

        private String subjectStatus;

        private String remarks;
    }
}