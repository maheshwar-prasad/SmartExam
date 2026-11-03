package com.smartexam.institution.course.dto;

import com.smartexam.institution.course.enums.CourseType;

public record CourseResponse(

        Long id,

        Long organizationId,

        Long campusId,

        Long departmentId,

        Long programId,

        String name,

        String code,

        String description,

        Integer credits,

        Integer semester,

        CourseType courseType,

        Boolean active
) {
}