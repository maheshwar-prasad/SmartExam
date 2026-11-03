package com.smartexam.gradingsystem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record GradingSystemRequest(
        @NotNull
        Long organizationId,

        @NotBlank
        @Size(max = 200)
        String name,

        @NotBlank
        @Size(max = 100)
        String code,

        @Size(max = 1000)
        String description,

        @NotNull
        Boolean active,

        @NotNull
        @Size(min = 1, message = "At least one grade band is required")
        List<@Valid GradingGradeRequest> grades
) {}