package com.smartexam.examschedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamScheduleResponse {

    private Long id;

    private Long organizationId;

    private Long examId;

    private Long examSubjectId;

    private LocalDate examDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Long roomId;

    private String remarks;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}