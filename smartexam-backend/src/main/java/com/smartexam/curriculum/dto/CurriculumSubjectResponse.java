package com.smartexam.curriculum.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CurriculumSubjectResponse(

        Long id,

        Long curriculumId,

        Long classSubjectId,

        Long subjectId,

        String subjectName,

        String subjectCode,

        Integer sequenceNo,

        BigDecimal theoryMarks,

        BigDecimal practicalMarks,

        BigDecimal passingMarks,

        Boolean active,

        LocalDateTime createdAt,

        Long createdBy,

        LocalDateTime updatedAt,

        Long updatedBy

) {}