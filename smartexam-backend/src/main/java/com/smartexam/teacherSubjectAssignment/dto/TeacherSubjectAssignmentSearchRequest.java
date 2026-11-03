package com.smartexam.teacherSubjectAssignment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSubjectAssignmentSearchRequest {

    @Builder.Default
    private String keyword = "";

    private Long teacherId;

    private Long subjectId;

    private Long classRoomId;

    private Long sectionId;

    private Long academicSessionId;

    private Boolean classTeacher;

    private Boolean active;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;

    @Builder.Default
    private String sortBy = "teacher.firstName";

    @Builder.Default
    private String direction = "ASC";

}