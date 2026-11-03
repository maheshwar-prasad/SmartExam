package com.smartexam.attendance.dto;

import com.smartexam.common.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceUpdateRequest {

    private LocalDate attendanceDate;

    private Long studentEnrollmentId;

    private Long teacherSubjectAssignmentId;

    private AttendanceStatus status;

    private String remarks;

    private Boolean active;

}