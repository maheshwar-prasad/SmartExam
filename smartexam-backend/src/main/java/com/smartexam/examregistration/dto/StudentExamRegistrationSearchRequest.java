package com.smartexam.examregistration.dto;

import lombok.Data;

@Data
public class StudentExamRegistrationSearchRequest {

    private Long organizationId;

    private Long studentId;

    private Long examId;

    private String registrationNumber;

    private String status;

    private Boolean active;
}