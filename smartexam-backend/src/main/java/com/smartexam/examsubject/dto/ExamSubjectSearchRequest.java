package com.smartexam.examsubject.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExamSubjectSearchRequest {

    private Long organizationId;

    private Long examId;

    private Long subjectId;

    private LocalDate examDate;

    private Boolean active;
}