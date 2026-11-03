package com.smartexam.classsubject.dto;

import jakarta.validation.constraints.NotNull;

public record ClassSubjectUpdateRequest(

        @NotNull(message = "Organization is required")
        Long organizationId,

        @NotNull(message = "Academic session is required")
        Long academicSessionId,

        @NotNull(message = "Class is required")
        Long classRoomId,

        @NotNull(message = "Subject is required")
        Long subjectId,

        Boolean active

) {
}