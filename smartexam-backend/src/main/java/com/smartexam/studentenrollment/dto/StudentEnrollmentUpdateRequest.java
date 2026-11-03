package com.smartexam.studentenrollment.dto;

import com.smartexam.common.enums.EnrollmentStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrollmentUpdateRequest {

    private Long studentId;

    private Long academicSessionId;

    private Long classRoomId;

    private Long sectionId;

    private String rollNo;

    private LocalDate admissionDate;

    private EnrollmentStatus enrollmentStatus;

    private Boolean active;

}