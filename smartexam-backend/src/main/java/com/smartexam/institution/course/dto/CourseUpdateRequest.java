package com.smartexam.institution.course.dto;

import com.smartexam.institution.course.enums.CourseType;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CourseUpdateRequest(

        Long campusId,

        Long departmentId,

        Long programId,

        @Size(max = 150)
        String name,

        @Size(max = 30)
        String code,

        @Size(max = 500)
        String description,

        @Positive
        Integer credits,

        @Positive
        Integer semester,

        CourseType courseType,

        Boolean active
) {
}