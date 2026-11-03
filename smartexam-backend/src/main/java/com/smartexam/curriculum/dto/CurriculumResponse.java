package com.smartexam.curriculum.dto;

import java.time.LocalDateTime;

public record CurriculumResponse(

        Long id,

        Long organizationId,

        String organizationName,

        Long academicSessionId,

        String academicSessionName,

        Long classRoomId,

        String classRoomName,

        String classRoomCode,

        String name,

        String code,

        String description,

        Boolean active,

        LocalDateTime createdAt,

        Long createdBy,

        LocalDateTime updatedAt,

        Long updatedBy

) {}