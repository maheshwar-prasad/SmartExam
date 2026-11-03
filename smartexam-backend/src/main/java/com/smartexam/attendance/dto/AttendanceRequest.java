package com.smartexam.attendance.dto;

import com.smartexam.common.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {

    @NotNull(message = "Attendance Date is required")
    private LocalDate attendanceDate;

    @NotNull(message = "Student Enrollment is required")
    private Long studentEnrollmentId;

    @NotNull(message = "Teacher Subject Assignment is required")
    private Long teacherSubjectAssignmentId;

    @NotNull(message = "Attendance Status is required")
    private AttendanceStatus status;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

}