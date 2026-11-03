package com.smartexam.teacherSubjectAssignment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSubjectAssignmentUpdateRequest {

    private Long teacherId;

    private Long subjectId;

    private Long classRoomId;

    private Long sectionId;

    private Long academicSessionId;

    private Boolean classTeacher;

    private Boolean active;

}