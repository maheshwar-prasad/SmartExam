package com.smartexam.syllabus.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record SyllabusUpdateRequest(

        @NotNull
        Long organizationId,

        @NotNull
        Long curriculumId,

        @NotNull
        Long classSubjectId,

        @NotBlank
        @Size(max = 200)
        String title,

        @NotBlank
        @Size(max = 200)
        String chapterName,

        @Size(max = 300)
        String topicName,

        @Size(max = 1000)
        String description,

        @NotNull
        Integer sequenceNo,

        @DecimalMin("0.0")
        BigDecimal estimatedHours,

        Boolean active

) {
}