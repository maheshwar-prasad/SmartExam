package com.smartexam.examschedule.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExamScheduleSearchRequest {

    private Long organizationId;

    private Long examId;

    private Long examSubjectId;

    private LocalDate examDate;

    private Long roomId;

    private Boolean active;
}