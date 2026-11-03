package com.smartexam.attendance.dto;

import com.smartexam.common.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSearchRequest {

    @Builder.Default
    private String keyword = "";

    private Long studentEnrollmentId;

    private Long teacherSubjectAssignmentId;

    private Long studentId;

    private Long teacherId;

    private Long subjectId;

    private Long academicSessionId;

    private Long classRoomId;

    private Long sectionId;

    private AttendanceStatus status;

    private LocalDate attendanceDate;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Boolean active;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;

    @Builder.Default
    private String sortBy = "attendanceDate";

    @Builder.Default
    private String direction = "DESC";

}