package com.smartexam.attendance.dto;

import com.smartexam.common.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private Long id;

    // Attendance
    private LocalDate attendanceDate;

    private AttendanceStatus status;

    private String remarks;

    private Boolean active;

    // Student
    private Long studentEnrollmentId;

    private Long studentId;

    private String studentCode;

    private String admissionNo;

    private String rollNo;

    private String studentName;

    // Teacher
    private Long teacherSubjectAssignmentId;

    private Long teacherId;

    private String teacherCode;

    private String teacherName;

    // Subject
    private Long subjectId;

    private String subjectCode;

    private String subjectName;

    // Academic Session
    private Long academicSessionId;

    private String academicSessionName;

    // Class Room
    private Long classRoomId;

    private String classRoomCode;

    private String classRoomName;

    // Section
    private Long sectionId;

    private String sectionCode;

    private String sectionName;

}