package com.smartexam.syllabus.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SyllabusResponse(

        Long id,

        Long organizationId,
        String organizationName,

        Long curriculumId,
        String curriculumName,
        String curriculumCode,

        Long classSubjectId,

        Long subjectId,
        String subjectName,
        String subjectCode,

        String title,

        String chapterName,

        String topicName,

        String description,

        Integer sequenceNo,

        BigDecimal estimatedHours,

        Boolean active,

        LocalDateTime createdAt,
        Long createdBy,

        LocalDateTime updatedAt,
        Long updatedBy

) {
}