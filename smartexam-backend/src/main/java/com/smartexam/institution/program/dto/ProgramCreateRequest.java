package com.smartexam.institution.program.dto;

import com.smartexam.institution.program.enums.DurationUnit;
import com.smartexam.institution.program.enums.ProgramType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProgramCreateRequest(

        @NotNull(message = "Organization ID is required")
        Long organizationId,

        @NotNull(message = "Department ID is required")
        Long departmentId,

        @NotBlank(message = "Program name is required")
        @Size(
                max = 150,
                message = "Program name must not exceed 150 characters"
        )
        String name,

        @NotNull(message = "Program type is required")
        ProgramType programType,

        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be greater than zero")
        Integer duration,

        @NotNull(message = "Duration unit is required")
        DurationUnit durationUnit,

        String description
) {
}