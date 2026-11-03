package com.smartexam.studenttransfer.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentTransferResponse {

    private Long studentId;

    private String studentCode;

    private String admissionNo;

    private String studentName;

    private Long oldEnrollmentId;

    private Long newEnrollmentId;

    private Long sourceAcademicSessionId;

    private String sourceAcademicSessionName;

    private Long sourceClassRoomId;

    private String sourceClassRoomName;

    private Long sourceSectionId;

    private String sourceSectionName;

    private Long targetAcademicSessionId;

    private String targetAcademicSessionName;

    private Long targetClassRoomId;

    private String targetClassRoomName;

    private Long targetSectionId;

    private String targetSectionName;

    private LocalDate transferDate;

    private String reason;

    private String remarks;

    private String status;
}