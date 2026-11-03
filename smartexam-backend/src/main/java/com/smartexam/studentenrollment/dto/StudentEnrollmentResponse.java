package com.smartexam.studentenrollment.dto;

import com.smartexam.common.enums.EnrollmentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrollmentResponse {

    private Long id;

    // Student Details
    private Long studentId;
    private String studentCode;
    private String admissionNo;
    private String rollNo;
    private String studentName;

    private Long organizationId;
    private String organizationName;

    // Academic Session
    private Long academicSessionId;
    private String academicSessionName;

    // ClassRoom
    private Long classRoomId;
    private String classRoomCode;
    private String classRoomName;

    // Section
    private Long sectionId;
    private String sectionCode;
    private String sectionName;

    // Enrollment
    private LocalDate admissionDate;
    private EnrollmentStatus enrollmentStatus;

    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}