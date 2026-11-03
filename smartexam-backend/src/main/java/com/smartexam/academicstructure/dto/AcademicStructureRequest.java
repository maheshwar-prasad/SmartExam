package com.smartexam.academicstructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AcademicStructureRequest(

        @NotNull
        Long organizationId,

        @NotNull
        Long academicSessionId,

        @NotNull
        Long classRoomId,

        @NotNull
        Long sectionId,

        @NotBlank
        @Size(max = 200)
        String name,

        @NotBlank
        @Size(max = 100)
        String code,

        @Size(max = 1000)
        String description,

        Boolean active
) {
}