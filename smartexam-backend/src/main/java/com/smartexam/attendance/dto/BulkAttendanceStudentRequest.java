package com.smartexam.attendance.dto;

import com.smartexam.common.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class BulkAttendanceStudentRequest {

    @NotNull(message = "Student Enrollment is required")
    private Long studentEnrollmentId;

    @NotNull(message = "Attendance Status is required")
    private AttendanceStatus status;

    @Size(
            max = 500,
            message = "Remarks cannot exceed 500 characters")
    private String remarks;
}