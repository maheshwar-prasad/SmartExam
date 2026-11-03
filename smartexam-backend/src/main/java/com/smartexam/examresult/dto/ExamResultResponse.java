package com.smartexam.examresult.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultResponse {

    private Long id;

    private Long organizationId;

    private Long studentId;

    private Long examId;

    private Integer totalSubjects;

    private BigDecimal totalMaximumMarks;

    private BigDecimal totalObtainedMarks;

    private BigDecimal percentage;

    private String grade;

    private String resultStatus;

    private Boolean published;

    private Boolean active;

    private String remarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}