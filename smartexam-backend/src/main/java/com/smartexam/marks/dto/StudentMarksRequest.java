package com.smartexam.marks.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentMarksRequest {

    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotNull(message = "Exam Subject ID is required")
    private Long examSubjectId;

    @NotNull(message = "Obtained marks are required")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Obtained marks cannot be negative"
    )
    private BigDecimal obtainedMarks;

    private String remarks;
}