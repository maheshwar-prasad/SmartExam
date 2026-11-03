package com.smartexam.teacherSubjectAssignment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSubjectAssignmentResponse {

    private Long id;

    private Long teacherId;

    private String employeeCode;

    private String teacherName;

    private Long subjectId;

    private String subjectCode;

    private String subjectName;

    private Long classRoomId;

    private String classRoomCode;

    private String classRoomName;

    private Long sectionId;

    private String sectionCode;

    private String sectionName;

    private Long academicSessionId;

    private String academicSessionName;

    private Boolean classTeacher;

    private Boolean active;

}