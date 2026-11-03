package com.smartexam.academicstructure.dto;

import java.time.LocalDateTime;

public record AcademicStructureResponse(

        Long id,

        Long organizationId,
        String organizationName,

        Long academicSessionId,
        String academicSessionName,

        Long classRoomId,
        String classRoomName,
        String classRoomCode,

        Long sectionId,
        String sectionName,
        String sectionCode,

        String name,
        String code,
        String description,

        Boolean active,

        LocalDateTime createdAt,
        Long createdBy,

        LocalDateTime updatedAt,
        Long updatedBy
) {
}