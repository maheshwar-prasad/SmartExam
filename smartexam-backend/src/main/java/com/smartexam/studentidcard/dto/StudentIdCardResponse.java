package com.smartexam.studentidcard.dto;

import com.smartexam.studentidcard.enums.IdCardStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentIdCardResponse {

    private Long id;

    private Long studentId;

    private String studentName;

    private String studentCode;

    private String admissionNo;

    private String rollNo;

    private String className;

    private String sectionName;

    private String cardNumber;

    private String templateName;

    private Long academicSessionId;

    private IdCardStatus status;

    private String fileName;

    private String remarks;

    private Boolean active;

    private String createdAt;

    private String updatedAt;
}