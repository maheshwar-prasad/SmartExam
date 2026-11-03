package com.smartexam.examresult.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultSearchRequest {

    private Long organizationId;

    private Long studentId;

    private Long examId;

    private String resultStatus;

    private String grade;

    private Boolean published;

    private Boolean active;
}