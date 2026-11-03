package com.smartexam.examregistration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentExamRegistrationResponse {

    private Long id;

    private Long organizationId;

    private Long studentId;

    private Long examId;

    private String registrationNumber;

    private LocalDate registrationDate;

    private String status;

    private String remarks;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}