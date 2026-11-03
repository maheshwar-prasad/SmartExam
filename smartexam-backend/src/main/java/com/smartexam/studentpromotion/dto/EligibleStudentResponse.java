package com.smartexam.studentpromotion.dto;

import com.smartexam.common.enums.EnrollmentStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibleStudentResponse {

    private Long studentId;

    private Long enrollmentId;

    private String studentCode;

    private String admissionNo;

    private String studentName;

    private String rollNo;

    private Long academicSessionId;

    private String academicSessionName;

    private Long classRoomId;

    private String classRoomName;

    private Long sectionId;

    private String sectionName;

    private LocalDate admissionDate;

    private EnrollmentStatus enrollmentStatus;

    private Boolean active;
}