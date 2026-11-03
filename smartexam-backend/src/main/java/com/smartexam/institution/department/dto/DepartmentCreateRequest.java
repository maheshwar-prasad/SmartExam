
package com.smartexam.institution.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentCreateRequest(

        @NotNull(message = "Organization ID is required")
        Long organizationId,

        Long campusId,

        @NotBlank(message = "Department name is required")
        @Size(
                max = 150,
                message = "Department name cannot exceed 150 characters"
        )
        String name,

        @NotBlank(message = "Department code is required")
        @Size(
                max = 50,
                message = "Department code cannot exceed 50 characters"
        )
        String code,

        @Size(
                max = 500,
                message = "Description cannot exceed 500 characters"
        )
        String description

) {
}