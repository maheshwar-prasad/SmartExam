package com.smartexam.classsubject.dto;

public record ClassSubjectSearchRequest(

        String keyword,

        Long organizationId,

        Long academicSessionId,

        Long classRoomId,

        Long subjectId,

        Boolean active,

        Integer page,

        Integer size,

        String sortBy,

        String direction

) {
}