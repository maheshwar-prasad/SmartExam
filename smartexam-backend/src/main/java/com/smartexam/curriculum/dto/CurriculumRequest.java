package com.smartexam.curriculum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CurriculumRequest(

        @NotNull
        Long organizationId,

        @NotNull
        Long academicSessionId,

        @NotNull
        Long classRoomId,

        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Size(max = 50)
        String code,

        @Size(max = 500)
        String description,

        Boolean active

) {}