package com.smartexam.teacherSubjectAssignment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSubjectAssignmentRequest {

    @NotNull(message = "Teacher is required")
    private Long teacherId;

    @NotNull(message = "Subject is required")
    private Long subjectId;

    @NotNull(message = "Class Room is required")
    private Long classRoomId;

    @NotNull(message = "Section is required")
    private Long sectionId;

    @NotNull(message = "Academic Session is required")
    private Long academicSessionId;

    @Builder.Default
    private Boolean classTeacher = false;

}