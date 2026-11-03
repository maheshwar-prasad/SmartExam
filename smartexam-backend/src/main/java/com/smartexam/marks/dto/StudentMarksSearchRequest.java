package com.smartexam.marks.dto;

import lombok.Data;

@Data
public class StudentMarksSearchRequest {

    private Long organizationId;

    private Long studentId;

    private Long examId;

    private Long examSubjectId;

    private String grade;

    private Boolean published;

    private Boolean active;
}