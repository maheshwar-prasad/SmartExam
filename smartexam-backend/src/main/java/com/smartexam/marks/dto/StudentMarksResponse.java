package com.smartexam.marks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentMarksResponse {

    private Long id;

    private Long organizationId;

    private Long studentId;

    private Long examId;

    private Long examSubjectId;

    private BigDecimal maximumMarks;

    private BigDecimal obtainedMarks;

    private String grade;

    private String remarks;

    private Boolean published;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}