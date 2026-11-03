package com.smartexam.classsubject.dto;

import java.time.LocalDateTime;

public record ClassSubjectResponse(

        Long id,

        Long organizationId,
        String organizationName,

        Long academicSessionId,
        String academicSessionName,

        Long classRoomId,
        String classRoomName,

        Long subjectId,
        String subjectName,
        String subjectCode,

        Boolean active,

        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}