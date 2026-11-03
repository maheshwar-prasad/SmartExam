package com.smartexam.examsubject.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class ExamSubjectResponse {

    private Long id;

    private Long organizationId;

    private Long examId;

    private Long subjectId;

    private LocalDate examDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private BigDecimal maximumMarks;

    private BigDecimal passingMarks;

    private String remarks;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}