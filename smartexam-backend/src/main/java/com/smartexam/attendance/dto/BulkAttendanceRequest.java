package com.smartexam.attendance.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkAttendanceRequest {

    @NotNull(message = "Attendance Date is required")
    private LocalDate attendanceDate;

    @NotNull(message = "Teacher Subject Assignment is required")
    private Long teacherSubjectAssignmentId;

    @NotEmpty(message = "Student attendance list cannot be empty")
    @Valid
    private List<BulkAttendanceStudentRequest> students;
}