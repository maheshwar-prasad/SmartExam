package com.smartexam.studentenrollment.dto;

import com.smartexam.common.enums.EnrollmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrollmentRequest {

    @NotNull(message = "Student is required")
    private Long studentId;

    @NotNull(message = "Academic Session is required")
    private Long academicSessionId;

    @NotNull(message = "Class Room is required")
    private Long classRoomId;

    @NotNull(message = "Section is required")
    private Long sectionId;

    @NotBlank(message = "Roll Number is required")
    private String rollNo;

    @NotNull(message = "Admission Date is required")
    private LocalDate admissionDate;

    @NotNull(message = "Enrollment Status is required")
    private EnrollmentStatus enrollmentStatus;

}