package com.smartexam.curriculum.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CurriculumSubjectRequest(

        @NotNull
        Long classSubjectId,

        @NotNull
        Integer sequenceNo,

        @DecimalMin("0.0")
        BigDecimal theoryMarks,

        @DecimalMin("0.0")
        BigDecimal practicalMarks,

        @DecimalMin("0.0")
        BigDecimal passingMarks,

        Boolean active

) {}