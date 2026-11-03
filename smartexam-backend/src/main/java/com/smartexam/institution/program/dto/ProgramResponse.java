package com.smartexam.institution.program.dto;

import com.smartexam.institution.program.enums.DurationUnit;
import com.smartexam.institution.program.enums.ProgramType;

public record ProgramResponse(

        Long id,

        Long organizationId,

        Long departmentId,

        String name,

        String code,

        ProgramType programType,

        Integer duration,

        DurationUnit durationUnit,

        String description,

        Boolean active
) {
}