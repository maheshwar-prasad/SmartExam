package com.smartexam.exam.dto;

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
public class ExamResponse {

    private Long id;

    private Long organizationId;

    private Long academicSessionId;
    private String academicSessionName;

    private Long classRoomId;
    private String classRoomCode;
    private String classRoomName;

    private Long sectionId;
    private String sectionCode;
    private String sectionName;

    private String name;

    private String examType;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status;

    private String remarks;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}