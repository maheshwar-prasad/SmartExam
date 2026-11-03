package com.smartexam.examregistration.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentExamRegistrationRequest {

    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Exam ID is required")
    private Long examId;

    @Size(
            max = 100,
            message = "Registration number must not exceed 100 characters"
    )
    private String registrationNumber;

    private LocalDate registrationDate;

    @Size(
            max = 500,
            message = "Remarks must not exceed 500 characters"
    )
    private String remarks;
}