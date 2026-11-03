package com.smartexam.examsubject.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ExamSubjectRequest {

    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Exam date is required")
    private LocalDate examDate;

    private LocalTime startTime;

    private LocalTime endTime;

    @NotNull(message = "Maximum marks is required")
    @DecimalMin(
            value = "0.01",
            message = "Maximum marks must be greater than zero"
    )
    private BigDecimal maximumMarks;

    @NotNull(message = "Passing marks is required")
    @DecimalMin(
            value = "0.00",
            message = "Passing marks cannot be negative"
    )
    private BigDecimal passingMarks;

    private String remarks;
}