package com.smartexam.institution.course.dto;

import com.smartexam.institution.course.enums.CourseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CourseCreateRequest(

        @NotNull
        Long organizationId,

        @NotNull
        Long campusId,

        @NotNull
        Long departmentId,

        @NotNull
        Long programId,

        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Size(max = 30)
        String code,

        @Size(max = 500)
        String description,

        @Positive
        Integer credits,

        @Positive
        Integer semester,

        @NotNull
        CourseType courseType,

        Boolean active
) {
}